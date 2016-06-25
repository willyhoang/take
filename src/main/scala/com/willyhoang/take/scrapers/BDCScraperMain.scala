package com.willyhoang.take.scrapers

/**
 * Main class to run the BDC SCraper class.
 */
object BDCScraperMain {

  def main(args: Array[String]) : Unit = {
    val classes = BDCScraper.scrape(args(0))
    println(classes)

    val parsedClasses = classes.flatMap(BDCParser.parse(args(0), _)).map(_.toNormalizedClass())
    println(parsedClasses)

//    val normalizedClasses = parsedClasses.map(ClassConverter.convertClass(_))
//    println(normalizedClasses)
  }

}
