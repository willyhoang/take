package com.willyhoang.take.scrapers

object EXPGScraperUtil {

  def getClasses(date: String): Seq[NormalizedClass] = {
    val rawText = EXPGScraper.scrape(date)
    val parsedClasses = rawText.flatMap(EXPGParser.parse(date, _)).map(_.toNormalizedClass())
    parsedClasses
  }

}
