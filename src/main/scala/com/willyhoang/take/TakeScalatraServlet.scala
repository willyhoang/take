package com.willyhoang.take


import com.willyhoang.take.scrapers.{PeridanceScraperUtil, EXPGScraperUtil, BDCScraperUtil}
import org.joda.time.LocalDate

class TakeScalatraServlet extends TakeWebAppStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

  get("/classes") {
    contentType = "text/html"
    val today = LocalDate.now().plusDays(1).toString("yyyy-MM-dd")
    val bdcClasses = BDCScraperUtil.getClasses(today)
    val expgClasses = EXPGScraperUtil.getClasses(today)
    val peridanceClasses = PeridanceScraperUtil.getClasses(today)

    val classes = (bdcClasses ++ expgClasses ++ peridanceClasses).sortBy(_.startTime.toDateTimeToday.getMillis)
    ssp("index", "body" -> classes)
  }
}
