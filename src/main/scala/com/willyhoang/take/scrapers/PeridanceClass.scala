package com.willyhoang.take.scrapers

import org.joda.time.{LocalDate, LocalTime}

/**
 * A class to represent a BDC class.
 */
case class PeridanceClass(date: String, time: String, level: String, teacher: String, style: String)
  extends GenericClass {

  val studioName = "Peridance"

  def parseTime(time: String): LocalTime = {
    val isPm = time.contains("pm") || time.contains("PM")
    val cleanedTime = time.replaceAll("(?i)pm", "")
      .replaceAll("(?i)am", "")
      .trim
    val parsedTime = LocalTime.parse(cleanedTime)
    val addTwelve = isPm && parsedTime.getHourOfDay != 12

    if (addTwelve) parsedTime.plusHours(12) else parsedTime
  }

  def parseTimes(time: String): (LocalTime, LocalTime) = {
    val times = time.split("-")
    val parsedStartTime = parseTime(times(0))
    val parsedEndTime = parseTime(times(1))
    (parsedStartTime, parsedEndTime)
  }

  def toNormalizedClass(): NormalizedClass = {
    val parsedDate = LocalDate.parse(date)
    val (startTime, endTime) = parseTimes(time)
    NormalizedClass(parsedDate, startTime, endTime, studioName, teacher, style, level)
  }
}