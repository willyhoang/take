package io.github.willyhoang.take.scrapers

import com.typesafe.scalalogging.LazyLogging

/**
 * A parser to convert raw text into a EXPGClass object.
 */
object PeridanceParser extends LazyLogging {

  def parse(date: String, input: Seq[String]): Option[PeridanceClass] = {
    if (input.length != 4) {
      logger.warn(s"Could not parse input as PeridanceClass: ${input}")
      None
    } else {
      Some(
        PeridanceClass(date, input(0), input(1), input(2), input(3))
      )
    }
  }

}
