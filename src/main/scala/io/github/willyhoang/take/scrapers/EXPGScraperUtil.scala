package io.github.willyhoang.take.scrapers

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object EXPGScraperUtil extends LazyLogging {

  def getClasses(date: String): Future[Seq[NormalizedClass]] = {
    Future {
      logger.info(s"Scraping EXPG classes for date: ${date}")
      val rawText = EXPGScraper.scrape(date)
      val parsedClasses = rawText.flatMap(EXPGParser.parse(date, _)).map(_.toNormalizedClass())
      logger.info(s"Finished scraping EXPG classes for date: ${date}")
      parsedClasses
    }
  }

}
