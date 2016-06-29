package io.github.willyhoang.take.scrapers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object EXPGScraperUtil {

  def getClasses(date: String): Future[Seq[NormalizedClass]] = {
    Future {
      val rawText = EXPGScraper.scrape(date)
      val parsedClasses = rawText.flatMap(EXPGParser.parse(date, _)).map(_.toNormalizedClass())
      parsedClasses
    }
  }

}
