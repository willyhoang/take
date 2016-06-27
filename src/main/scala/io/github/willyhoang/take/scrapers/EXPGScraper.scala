package io.github.willyhoang.take.scrapers

import org.joda.time.LocalDate
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.{By, WebElement}

import scala.collection.JavaConverters._

object EXPGScraper {
  var driver : ChromeDriver = _

  def clearInput(element: WebElement): Unit = {
    val originalValue = element.getAttribute("value")
    originalValue.toCharArray.foreach{x => element.sendKeys("\u0008")}
  }

  def scrape(date: String): Seq[Seq[String]] = {
    val html = getHtml(date)
    val doc: Document = Jsoup.parse(html)

    val elements: Elements = doc.select("#classSchedule-mainTable tbody tr")

    // first row is date, last row is regularly scheduled note
    val classRows: Seq[Element] = elements.subList(1, elements.size()).asScala
    classRows.map(_.children().asScala.map(_.text().replaceAll("\u00a0"," ").trim))
  }

  def getHtml(date: String): String = {
    val formattedDate = LocalDate.parse(date).toString("M/dd/yyyy")
    System.setProperty("webdriver.chrome.driver", "/Users/willy/Desktop/chromedriver")
    driver = new ChromeDriver()
    driver.get("https://clients.mindbodyonline.com/classic/home?studioid=177785")

    driver.switchTo().frame("mainFrame")

    val classesLink = driver.findElement(By.id("tabA102"))
    classesLink.click()

    val dayToggle = driver.findElement(By.id("day-tog-c"))
    dayToggle.click()

    val dateInput = driver.findElement(By.id("txtDate"))
    clearInput(dateInput)

    val textDateInput = driver.findElement(By.id("txtDate"))
    textDateInput.sendKeys(formattedDate)
    textDateInput.submit()
    val pageSource = driver.getPageSource
    driver.close()
    pageSource
  }
}
