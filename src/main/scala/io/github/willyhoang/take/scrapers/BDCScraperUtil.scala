package io.github.willyhoang.take.scrapers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object BDCScraperUtil {

  def getClasses(date: String): Future[Seq[NormalizedClass]] = {
    Future {
      val classes = BDCScraper.scrape(date)
      val parsedClasses = classes.flatMap(BDCParser.parse(date, _)).map(_.toNormalizedClass())

      parsedClasses
    }
  }

}
