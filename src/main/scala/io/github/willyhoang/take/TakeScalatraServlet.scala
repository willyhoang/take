package io.github.willyhoang.take

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import org.scalatra.guavaCache.GuavaCache
import org.scalatra.{CorsSupport, AsyncResult, FutureSupport}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class TakeScalatraServlet(system: ActorSystem, classesRetrieverActor: ActorRef)
  extends TakeStack with FutureSupport with CorsSupport {

  implicit val cacheBackend = GuavaCache
  implicit def executor: ExecutionContext = system.dispatcher
  implicit val defaultTimeout: Timeout = Timeout(60 seconds)

  import _root_.akka.pattern.ask

  options("/classes/:date") {
    response.setHeader("Access-Control-Allow-Headers",
      request.getHeader("Access-Control-Request-Headers"))
    response.setHeader("Access-Control-Allow-Origin", "*")
  }

  get("/") {
    contentType = "text/html"
    ssp("index", "body" -> "")
  }

  get("/classes/:date") {
    new AsyncResult {
      override implicit def timeout: Duration = 60 seconds
      val is = classesRetrieverActor ? Process(params("date"))
    }
  }
}
