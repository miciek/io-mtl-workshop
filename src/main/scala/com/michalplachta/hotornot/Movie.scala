package com.michalplachta.hotornot

import scala.util.Try

case class Movie(title: String, score: Option[Double])

object Movie {
  val random = util.Random
  def randomMovie(movies: List[Movie]): Option[Movie] = {
    Try(movies(random.nextInt(movies.size))).toOption
  }
}
