package com.willyhoang.take

import com.willyhoang.take.scrapers.BDCScraperUtil

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
    val classes = BDCScraperUtil.getClasses("2016-06-23")
     <html>
      <body>
        <h1>Classes</h1>
        {classes}
      </body>
    </html>
  }
}
