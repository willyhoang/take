package com.willyhoang.take


import com.willyhoang.take.scrapers.{EXPGScraperUtil, PeridanceScraperUtil, BDCScraperUtil}
import net.liftweb.json.Serialization.write
import net.liftweb.json.ext.JodaTimeSerializers
import net.liftweb.json.{NoTypeHints, Serialization}
import org.joda.time.LocalDate
import org.scalatra.Ok

class TakeScalatraServlet extends TakeWebAppStack {

  get("/") {
    contentType = "text/html"
    ssp("index", "body" -> "")
  }

  get("/classes/:date") {
    val date = params("date")

    LocalDate.parse(date)
    val bdcClasses = BDCScraperUtil.getClasses(date)
    val expgClasses = EXPGScraperUtil.getClasses(date)
    val peridanceClasses = PeridanceScraperUtil.getClasses(date)

    val classes = (bdcClasses ++ expgClasses ++ peridanceClasses).sortBy(_.startTime.toDateTimeToday.getMillis)

    implicit val formats = Serialization.formats(NoTypeHints) ++ JodaTimeSerializers.all
    val jsonClasses = write(classes)
    Ok(jsonClasses)
  }
}
