package io.github.willyhoang.take.scrapers

import com.typesafe.scalalogging.LazyLogging
import org.joda.time.LocalDate
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements
import org.jsoup.{HttpStatusException, Jsoup}

import scala.collection.JavaConverters._
import scala.collection.mutable.Buffer

/**
 * A scraper to pull class schedules from BDC.
 */
object BDCScraper extends LazyLogging {

  /**
   * The relevant fields for a class at BDC.
   */
  val time = "time"
  val style = "style"
  val level = "level"
  val instructor = "instructor"

  /**
   * Converts a blob of strings into a map from field name to value.
   *
   * @param strings The directly scraped strings
   * @return A map of class keys and values
   */
  def arrayToClassMap(strings: Buffer[String]) = {
    if (strings.length < 4) {
      None
    } else {
      Some(Map(
        (time, strings(0)),
        (style, strings(1)),
        (level, strings(2)),
        (instructor, strings(3))
      ))
    }
  }

  def formatDate(date: String): String = {
    val parsedDate = LocalDate.parse(date)
    return f"${parsedDate.getMonthOfYear}_${parsedDate.getDayOfMonth}"
  }

  /**
   * Scrapes the BDC website for class schedules by day.
   *
   * @param date The day to pull classes from (e.g. 6_23)
   * @return a sequence of class maps
   */
  def scrape(date: String): Seq[Seq[String]] = {
    val formattedDate = formatDate(date)

    try {
      val doc: Document = Jsoup.connect(s"http://broadwaydancecenter.com/schedule/$formattedDate.shtml").get()
      val elements: Elements = doc.select(".grid tbody tr")
      val dateText = elements.first().text()
      val classRows: Seq[Element] = elements.subList(1, elements.size()).asScala
      classRows.map(_.children().asScala.map(_.text()))
    } catch {
      case e: HttpStatusException => {
        logger.error("Failed to connect to broadwaycenter.com", e)
        List(List())
      }
    }
  }
}
