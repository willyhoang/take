package com.willyhoang.take.scrapers

/**
 *
 */
object BDCParser {

  /**
   * The relevant fields for a class at BDC.
   */
  val date = "date"
  val time = "time"
  val style = "style"
  val level = "level"
  val instructor = "instructor"

  /**
   * Converts a blob of strings into a BDCClass object.
 *
   * @param input The directly scraped strings
   * @return A BDCClass object
   */
  def parse(date: String, input: Seq[String]) : Option[BDCClass] = {
    if (input.length < 4) {
      None
    } else {
      Some(
        BDCClass(date, input(0), input(1), input(2), input(3))
      )
    }
  }

  /**
   * Sanitizes an input string (removing whitespace, new lines, and other extraneous characters
   *
   * @param input The input string
   * @return The sanitized string
   */
  def sanitize(input: String) : String = input
}
