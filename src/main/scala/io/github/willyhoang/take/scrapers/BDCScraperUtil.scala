package io.github.willyhoang.take.scrapers

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object BDCScraperUtil extends LazyLogging {
  def getClasses(date: String): Future[Seq[NormalizedClass]] = {
    Future {
      logger.info(s"Scraping classes for date: ${date}")
      val classes = BDCScraper.scrape(date)
      val parsedClasses = classes.flatMap(BDCParser.parse(date, _)).map(_.toNormalizedClass())

      parsedClasses
    }
  }

}
