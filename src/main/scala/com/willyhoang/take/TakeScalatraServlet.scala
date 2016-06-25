package com.willyhoang.take


import com.willyhoang.take.scrapers.BDCScraperUtil
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
    val today = LocalDate.now().toString("yyyy-MM-dd")
    val classes = BDCScraperUtil.getClasses(today)
//    ssp("classes", "body" -> classes)
    ssp("index", "body" -> classes)
  }
}
