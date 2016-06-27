package com.willyhoang.take.scrapers

import net.liftweb.json.Serialization.write
import net.liftweb.json.{Serialization, NoTypeHints}
import net.liftweb.json.ext.JodaTimeSerializers

object PeridanceScraperUtil {

  def getClasses(date: String): String = {
    val rawText = PeridanceScraper.scrape(date)
    val parsedClasses = rawText.flatMap(PeridanceParser.parse(date, _)).map(_.toNormalizedClass())
    implicit val formats = Serialization.formats(NoTypeHints) ++ JodaTimeSerializers.all
    write(parsedClasses)
  }

}
