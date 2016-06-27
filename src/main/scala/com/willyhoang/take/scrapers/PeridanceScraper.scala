package com.willyhoang.take.scrapers

import org.joda.time.LocalDate
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver

import scala.collection.JavaConverters._

object PeridanceScraper {

  def findGenre(index: Int, genres: Seq[(String, Int)]) = {
    genres.filter{ case (_, i) => i <= index}.last._1
  }

  def scrape(date: String) = {
    try {
      val html = getHtml(date)
      val doc: Document = Jsoup.parse(html)


      val rows: Elements = doc.select("table table tr")
      val texts = rows.asScala.map(_.children().asScala.map(_.text().replaceAll("\u00a0", " ").trim)).zipWithIndex
      val genres = texts.filter { case (row, _) => row.length == 1 }.map { case (row, i) => (row.head, i) }

      texts.filter { case (row, _) => row.length != 1 } // filter out genre rows
        .filter { case (row, _) => row(0) != "Time" } // filter out header rows
        .map { case (row, index) => row :+ findGenre(index, genres) } // add genre
    } catch {
      case e: IllegalArgumentException => {
        println(e)
        List(List())
      }
    }

  }

  def getHtml(date: String): String = {
    val parsedDate = LocalDate.parse(date)
    val year = parsedDate.getYear
    val day = parsedDate.getDayOfMonth
    val month = parsedDate.getMonthOfYear

    System.setProperty("webdriver.chrome.driver", "/Users/willy/Desktop/chromedriver")
    val driver = new ChromeDriver()
    driver.get("http://www.peridance.com/openclasses.cfm")


    var dateSelector = driver.findElements(By.cssSelector(s"td[data-month='${month -1}']" +
                                                          s"[data-year='${year}']"))
    // wrong month, try next
    while (dateSelector.isEmpty) {
      val nextButton = driver.findElement(By.cssSelector(".ui-datepicker-next"))

      // ensure the next button is enabled
      if (nextButton.getAttribute("class").contains("ui-state-disabled")) {
        throw new IllegalArgumentException(s"No data found for date: ${date}")
      }
      nextButton.click()
      dateSelector = driver.findElements(By.cssSelector(s"td[data-month='${month -1}']" +
                                                          s"[data-year='${year}']"))
    }

    // find desired date and click on it
    val desiredDate = dateSelector.asScala.filter(_.getText == day.toString)
    if (desiredDate.isEmpty) {
      throw new IllegalArgumentException(s"Cannot select the day provided: ${date}")
    }
    if (desiredDate.length != 1) {
       throw new IllegalArgumentException(s"Multiple day selectors found for: ${date}")
    }
    desiredDate.head.click()
    val pageSource = driver.getPageSource
    driver.close()
    pageSource
  }
}
