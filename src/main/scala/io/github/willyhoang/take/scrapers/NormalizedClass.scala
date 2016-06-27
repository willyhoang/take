package io.github.willyhoang.take.scrapers

import net.liftweb.json.Serialization.write
import net.liftweb.json.ext.JodaTimeSerializers
import net.liftweb.json.{NoTypeHints, Serialization}
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

  def toJson(): String = {
    implicit val formats = Serialization.formats(NoTypeHints) ++ JodaTimeSerializers.all
    write(this)
  }
}
