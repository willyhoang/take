package com.willyhoang.take


import com.willyhoang.take.scrapers.{BDCScraperUtil, EXPGScraperUtil, PeridanceScraperUtil}
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

  get("/classes/:date") {
    contentType = "text/html"
    val date = params("date")

    LocalDate.parse(date)
    val bdcClasses = BDCScraperUtil.getClasses(date)
    val expgClasses = EXPGScraperUtil.getClasses(date)
    val peridanceClasses = PeridanceScraperUtil.getClasses(date)

    val classes = (bdcClasses ++ expgClasses ++ peridanceClasses).sortBy(_.startTime.toDateTimeToday.getMillis)
//    val classes:Seq[NormalizedClass] = List()
    ssp("index", "body" -> classes)
  }
}
