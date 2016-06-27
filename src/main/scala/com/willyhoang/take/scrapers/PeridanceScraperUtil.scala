package com.willyhoang.take.scrapers

object PeridanceScraperUtil {

  def getClasses(date: String): Seq[NormalizedClass] = {
    val rawText = PeridanceScraper.scrape(date)
    val parsedClasses = rawText.flatMap(PeridanceParser.parse(date, _)).map(_.toNormalizedClass())
    parsedClasses
  }

}
