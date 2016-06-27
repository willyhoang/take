package io.github.willyhoang.take

import io.github.willyhoang.take.scrapers.{PeridanceScraperUtil, EXPGScraperUtil, BDCScraperUtil}
import net.liftweb.json.Serialization.write
import net.liftweb.json.ext.JodaTimeSerializers
import net.liftweb.json.{NoTypeHints, Serialization}
import org.joda.time.LocalDate
import org.scalatra.Ok
import org.scalatra.cache.CacheSupport
import org.scalatra.guavaCache.GuavaCache

import scala.concurrent.duration.{Duration, HOURS}

class TakeScalatraServlet extends TakeStack with CacheSupport {

  implicit val cacheBackend = GuavaCache

  get("/") {
    contentType = "text/html"
    ssp("index", "body" -> "")
  }

  get("/classes/:date") {
    val expirationTime = Some(Duration(2, HOURS))
    cached(expirationTime) {
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
}
