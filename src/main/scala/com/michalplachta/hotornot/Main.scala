package com.michalplachta.hotornot

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.Random

object Main extends App {
  def fetchMovies(): List[Movie] = {
    println("Loading movies...")
    val movies = MovieMetadataParsing.parseMoviesFromResource(s"movie_metadata_${Random.nextInt(3) + 1}.csv")
    println(s"Loaded ${movies.size} movies.")
    movies
  }

  @tailrec
  def nextSession(movies: List[Movie], previousWinner: Option[Movie]): Unit = {
    val movieA = previousWinner.getOrElse(movies(Random.nextInt(movies.size)))
    val movieB = movies(Random.nextInt(movies.size))

    println(s" > ${movieA.title} (a) or ${movieB.title} (b) or ? (q to quit)")
    val c = StdIn.readChar().toLower
    if (c == 'q') System.exit(0)
    val winner = c match {
      case 'a' => Some(movieA)
      case 'b' => Some(movieB)
      case _   => None
    }
    println(s"You chose ${winner.map(_.title).getOrElse("nothing")}")
    nextSession(fetchMovies(), winner)
  }

  nextSession(fetchMovies(), None)
}
