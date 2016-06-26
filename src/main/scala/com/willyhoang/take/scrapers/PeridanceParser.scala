package com.willyhoang.take.scrapers

/**
 * A parser to convert raw text into a EXPGClass object.
 */
object PeridanceParser {

  def parse(date: String, input: Seq[String]): Option[PeridanceClass] = {
    if (input.length != 4) {
      println("Could not parse input as PeridanceClass", input)
      None
    } else {
      Some(
        PeridanceClass(date, input(0), input(1), input(2), input(3))
      )
    }
  }

}
