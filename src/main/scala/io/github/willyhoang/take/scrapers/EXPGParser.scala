package io.github.willyhoang.take.scrapers

import com.typesafe.scalalogging.LazyLogging

/**
 * A parser to convert raw text into a EXPGClass object.
 */
object EXPGParser extends LazyLogging {

  def parse(date: String, input: Seq[String]): Option[EXPGClass] = {
    if (input.length != 6) {
      logger.warn(s"Could not parse input as EXPGClass: ${input}")
      None
    } else {
      Some(
        EXPGClass(date, input(0), input(2), input(3), input(4), input(5))
      )
    }
  }

}
