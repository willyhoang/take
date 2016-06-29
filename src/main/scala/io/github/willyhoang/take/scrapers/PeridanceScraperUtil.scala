package io.github.willyhoang.take.scrapers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object PeridanceScraperUtil {

  def getClasses(date: String): Future[Seq[NormalizedClass]] = {
    Future {
      val rawText = PeridanceScraper.scrape(date)
      val parsedClasses = rawText.flatMap(PeridanceParser.parse(date, _)).map(_.toNormalizedClass())
      parsedClasses
    }
  }

}
