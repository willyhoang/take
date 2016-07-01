package io.github.willyhoang.take.scrapers

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object PeridanceScraperUtil extends LazyLogging {

  def getClasses(date: String): Future[Seq[NormalizedClass]] = {
    Future {
      logger.info(s"Scraping Peridance classes for date: ${date}")
      val rawText = PeridanceScraper.scrape(date)
      val parsedClasses = rawText.flatMap(PeridanceParser.parse(date, _)).map(_.toNormalizedClass())
      logger.info(s"Finished Peridance scraping classes for date: ${date}")
      parsedClasses
    }
  }

}
