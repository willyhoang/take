package io.github.willyhoang.take.scrapers


object BDCScraperUtil {

  def getClasses(date: String): Seq[NormalizedClass] = {
    val classes = BDCScraper.scrape(date)
    val parsedClasses = classes.flatMap(BDCParser.parse(date, _)).map(_.toNormalizedClass())

    parsedClasses
  }

}
