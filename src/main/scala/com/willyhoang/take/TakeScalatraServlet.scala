package com.willyhoang.take


import com.willyhoang.take.scrapers.BDCScraperUtil
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
//    val expgClasses = EXPGScraperUtil.getClasses(date)
//    val peridanceClasses = PeridanceScraperUtil.getClasses(date)

//    val classes = (bdcClasses ++ expgClasses ++ peridanceClasses).sortBy(_.startTime.toDateTimeToday.getMillis)

//    val classes:Seq[NormalizedClass] = List()
    val classes = bdcClasses
    Ok(classes)
  }
}
