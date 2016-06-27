package com.willyhoang.take.scrapers

import net.liftweb.json.Serialization.write
import net.liftweb.json.ext.JodaTimeSerializers
import net.liftweb.json.{NoTypeHints, Serialization}
object BDCScraperUtil {

  def getClasses(date: String): String = {
    val classes = BDCScraper.scrape(date)
    val parsedClasses = classes.flatMap(BDCParser.parse(date, _)).map(_.toNormalizedClass())

    implicit val formats = Serialization.formats(NoTypeHints) ++ JodaTimeSerializers.all
    write(parsedClasses)
  }

}
