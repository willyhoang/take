package com.willyhoang.take.scrapers

import org.joda.time.{LocalDate, LocalTime}

/**
 * A generic normalized class type that will be our final shared representation.
 */
case class NormalizedClass(date: LocalDate,
                           startTime: LocalTime,
                           endTime: LocalTime,
                           studio: String,
                           instructor: String,
                           style: String,
                           level: String) {

}
