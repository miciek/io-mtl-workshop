package com.michalplachta.hotornot

import cats.ApplicativeError
import cats.data.{EitherT, StateT}
import cats.effect.concurrent.{Deferred, Ref, TryableDeferred}
import cats.effect.{Concurrent, ContextShift, ExitCode, Fiber, IO, IOApp, Sync}
import cats.mtl.MonadState
import cats.implicits._
import com.olegpy.meow.effects._

import scala.io.StdIn
import scala.util.Random

object Main extends IOApp {
  def fetchMovies[F[_]: Sync: ApplicativeError[*[_], Throwable]]: F[List[Movie]] = {
    (for {
      _ <- Sync[F].delay(println(s"Loading movies..."))
      movies <- Sync[F].delay(
                 MovieMetadataParsing.parseMoviesFromResource(s"movie_metadata_${Random.nextInt(3) + 1}.csv")
               )
      _ <- Sync[F].delay(println(s"Loaded ${movies.size} movies."))
    } yield movies).handleErrorWith(_ => fetchMovies[F])
  }

  def updateMovieRepo[F[_]: Concurrent: ApplicativeError[*[_], Throwable]](
      movieRepo: TryableDeferred[F, Ref[F, List[Movie]]]
  ): F[Fiber[F, Unit]] = {
    Concurrent[F].start {
      for {
        movies   <- fetchMovies
        maybeRef <- movieRepo.tryGet
        _ <- maybeRef match {
              case Some(r) =>
                r.set(movies)
              case None =>
                Ref.of[F, List[Movie]](movies) >>= movieRepo.complete
            }
        _ <- updateMovieRepo(movieRepo)
      } yield ()
    }
  }

  def choose[F[_]: Sync: ApplicativeError[*[_], Throwable]](movieA: Movie,
                                                            movieB: Movie): F[Either[QuitTheGame.type, Movie]] = {
    (for {
      _ <- Sync[F].delay(println(s" > ${movieA.title} (a) or ${movieB.title} (b) or ? (q to quit)"))
      c <- Sync[F].delay(StdIn.readChar().toLower)
      result <- c match {
                 case 'a' => Sync[F].pure(movieA.asRight[QuitTheGame.type])
                 case 'b' => Sync[F].pure(movieB.asRight[QuitTheGame.type])
                 case 'q' => Sync[F].pure(QuitTheGame.asLeft[Movie])
                 case _ =>
                   ApplicativeError[F, Throwable].raiseError[Either[QuitTheGame.type, Movie]](InvalidOptionSelected)
               }
    } yield result).handleErrorWith(_ => choose(movieA, movieB))
  }

  def handleSessionResults[F[_]: Sync: MonadState[*[_], Option[Movie]]](winner: Movie): F[Unit] = {
    for {
      _ <- Sync[F].delay(println(s"You chose ${winner.title}"))
      _ <- MonadState[F, Option[Movie]].set(Some(winner))
    } yield ()
  }

  def session[F[_]: Sync: ApplicativeError[*[_], Throwable]: MonadState[*[_], Option[Movie]]](
      movieRepo: Deferred[F, Ref[F, List[Movie]]]
  ): F[Unit] = {
    for {
      previousWinner <- MonadState[F, Option[Movie]].get
      deferredMovies <- movieRepo.get
      movies         <- deferredMovies.get
      movieA         = previousWinner.getOrElse(movies(Random.nextInt(movies.size)))
      movieB         = movies(Random.nextInt(movies.size))
      quitOrWinner   <- choose[F](movieA, movieB)
      _ <- quitOrWinner.fold(
            _ => Sync[F].unit,
            winner => handleSessionResults[F](winner) >> session[F](movieRepo)
          )
    } yield ()
  }

  override def run(args: List[String]): IO[ExitCode] = {
    type Effect[A] = EitherT[IO, QuitTheGame.type, A]

    (for {
      movieRepo   <- Deferred.tryable[Effect, Ref[Effect, List[Movie]]]
      updateFiber <- updateMovieRepo(movieRepo)
      state       <- Ref.of[Effect, Option[Movie]](None)
      _ <- state.runState { implicit monadState =>
            session[Effect](movieRepo)
          }
    } yield ()).value.map(_ => ExitCode.Success)
  }
}
