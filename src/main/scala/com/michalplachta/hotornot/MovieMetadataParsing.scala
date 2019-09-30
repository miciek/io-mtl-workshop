package com.michalplachta.hotornot

import scala.io.Source
import scala.util.Try

object MovieMetadataParsing {
  def parseMoviesFromResource(resourceName: String): List[Movie] = {
    val bufferedSource = Source.fromResource(resourceName)

    val rawMovieLines = bufferedSource.getLines().toList
    bufferedSource.close
    val resultSet = if (rawMovieLines.nonEmpty) {
      val keys            = rawMovieLines.head.split(",").map(_.trim).toList
      val movieTitleIndex = keys.indexOf("movie_title")
      val scoreIndex      = keys.indexOf("imdb_score")
      rawMovieLines
        .drop(1)
        .map { line =>
          val cols = line.split(",").map(_.trim)
          Movie(cols(movieTitleIndex), Try(cols(scoreIndex).toDouble).toOption)
        }
        .toSet
    } else Set.empty[Movie]
    resultSet.toList
  }
}
