package io.github.willyhoang.take.scrapers

import org.joda.time.{LocalDate, LocalTime}

/**
 * A class to represent a BDC class.
 */
case class EXPGClass(date: String, startTime: String, className: String, teacher: String,
                     room: String, duration: String)
  extends GenericClass {

  val studioName = "EXPG"

  def parseTime(): (LocalTime, LocalTime) = {
    val addTwelve = !startTime.contains("12:") && (startTime.contains("pm") || startTime.contains("PM"))

    val cleanedTime = startTime.replaceAll("(?i)am", "").replaceAll("(?i)pm", "").trim
    val parsedTime = LocalTime.parse(cleanedTime)
    val actualStartTime = if (addTwelve) parsedTime.plusHours(12) else parsedTime

    val durationHoursMinutes = "(\\d+) hours? & (\\d+) minutes?".r
    val durationHours = "(\\d+) hours?".r

    val (numHour, numMinutes): (Int, Int) = duration match {
      case durationHoursMinutes(h, m) => (h.toInt, m.toInt)
      case durationHours(h) => (h.toInt, 0)
      case _ => throw new IllegalArgumentException(s"No regex matched for this duration: ${duration}")
    }

    (actualStartTime, actualStartTime.plusHours(numHour).plusMinutes(numMinutes))
  }

  def parseClass(): (String, String) = {
    val styleLevelRe = "(.+)\\((.+)\\)".r

    val cleanedClass = className.replaceAll("\uFF09", ")").replaceAll("\uFF08","(")
    cleanedClass match {
      case styleLevelRe(s, l) => (s, l)
      case s => (s, "")
    }
  }

  def toNormalizedClass(): NormalizedClass = {
    val parsedDate = LocalDate.parse(date)
    val (startTime, endTime) = parseTime()
    val (style, level) = parseClass()
    NormalizedClass(parsedDate, startTime, endTime, studioName, teacher, style, level)
  }
}