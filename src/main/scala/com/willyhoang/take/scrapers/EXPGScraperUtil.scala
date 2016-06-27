package com.willyhoang.take.scrapers

import net.liftweb.json.Serialization.write
import net.liftweb.json.{Serialization, NoTypeHints}
import net.liftweb.json.ext.JodaTimeSerializers

object EXPGScraperUtil {

  def getClasses(date: String): String = {
    val rawText = EXPGScraper.scrape(date)
    val parsedClasses = rawText.flatMap(EXPGParser.parse(date, _)).map(_.toNormalizedClass())
    implicit val formats = Serialization.formats(NoTypeHints) ++ JodaTimeSerializers.all
    write(parsedClasses)
  }

}
