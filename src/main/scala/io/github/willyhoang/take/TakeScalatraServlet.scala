package io.github.willyhoang.take

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import org.scalatra.cache.CacheSupport
import org.scalatra.guavaCache.GuavaCache
import org.scalatra.{AsyncResult, FutureSupport}

import scala.concurrent.ExecutionContext

class TakeScalatraServlet(system: ActorSystem, myActor: ActorRef) extends TakeStack with
  CacheSupport with FutureSupport {

  implicit val cacheBackend = GuavaCache
  implicit def executor: ExecutionContext = system.dispatcher

  import _root_.akka.pattern.ask
  implicit val defaultTimeout = Timeout(1000 * 60)


  get("/") {
    contentType = "text/html"
    ssp("index", "body" -> "")
  }

//  get("/classes/:date") {
//    val expirationTime = Some(Duration(2, HOURS))
//    cached(expirationTime) {
//      val date = params("date")
//
//      LocalDate.parse(date)
//      val bdcClasses = BDCScraperUtil.getClasses(date)
//      val expgClasses = EXPGScraperUtil.getClasses(date)
//      val peridanceClasses = PeridanceScraperUtil.getClasses(date)
//
//      val classes = (bdcClasses ++ expgClasses ++ peridanceClasses).sortBy(_.startTime.toDateTimeToday.getMillis)
//
//      implicit val formats = Serialization.formats(NoTypeHints) ++ JodaTimeSerializers.all
//      val jsonClasses = write(classes)
//      Ok(jsonClasses)
//    }
//  }

  get("/classes/:date") {
    new AsyncResult { val is =
      myActor ? Process(params("date"))
    }
  }
}
