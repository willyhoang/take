package com.willyhoang.take.scrapers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.openqa.selenium.chrome.ChromeDriver

import scala.collection.JavaConverters._

object PeridanceScraper {

  def findGenre(index: Int, genres: Seq[(String, Int)]) = {
    genres.filter{ case (_, i) => i <= index}.last._1
  }

  def scrape(date: String) = {
    val html = getHtml(date)
    println(html)
    val doc: Document = Jsoup.parse(html)


    val rows: Elements = doc.select("table table tr")
    val texts = rows.asScala.map(_.children().asScala.map(_.text().replaceAll("\u00a0"," ").trim)).zipWithIndex
    val genres = texts.filter{ case (row, _) => row.length == 1 }.map{ case (row, i) => (row.head, i) }

    texts.filter{ case (row, _) => row.length != 1 } // filter out genre rows
         .filter{ case (row, _) => row(0) != "Time"} // filter out header rows
         .map{ case (row, index) => row :+ findGenre(index, genres) } // add genre

  }

  def getHtml(date: String): String = {
    System.setProperty("webdriver.chrome.driver", "/Users/willy/Desktop/chromedriver")
    val driver = new ChromeDriver()
    driver.get("http://www.peridance.com/openclasses.cfm")
    driver.getPageSource
  }
}
