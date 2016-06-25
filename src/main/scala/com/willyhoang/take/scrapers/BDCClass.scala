package com.willyhoang.take.scrapers

import org.joda.time.{LocalDate, LocalTime}

/**
 * A class to represent a BDC class.
 */
case class BDCClass(date: String, time: String, style: String, level: String, instructor: String)
  extends GenericClass {

  val studioName = "BDC"

  def parseTime(time: String): (LocalTime, LocalTime) = {
    val isPm = time.contains("pm") || time.contains("PM")
    val times = time.replaceAll("(?i)pm", "")
      .replaceAll("(?i)am", "")
      .replaceAll("(?i)Noon", "12:00")
      .replaceAll("(?i)Midnight", "00:00")
      .split("-")
      .map(_.trim)

    val parsedStartTime = LocalTime.parse(times(0))
    val parsedEndTime = LocalTime.parse(times(1))

    isPm match {
      case true => (times(0), times(1)) match {
        case ("12:00", _) => (parsedStartTime, parsedEndTime.plusHours(12))
        case (_, "00:00") => (parsedStartTime.plusHours(12), parsedEndTime)
        case _ => (parsedStartTime.plusHours(12), parsedEndTime.plusHours(12))
      }
      case false => (parsedStartTime, parsedEndTime)
    }
  }

  def toNormalizedClass(): NormalizedClass = {
    val parsedDate = LocalDate.parse(date)
    val (startTime, endTime) = parseTime(time)
    NormalizedClass(parsedDate, startTime, endTime, studioName, instructor, style, level)
  }
}