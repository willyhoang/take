package io.github.willyhoang.take

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import org.scalatra.guavaCache.GuavaCache
import org.scalatra.{AsyncResult, FutureSupport}

import scala.concurrent.ExecutionContext

class TakeScalatraServlet(system: ActorSystem, classesRetrieverActor: ActorRef)
  extends TakeStack with FutureSupport {

  implicit val cacheBackend = GuavaCache
  implicit def executor: ExecutionContext = system.dispatcher

  import _root_.akka.pattern.ask
  implicit val defaultTimeout = Timeout(1000 * 60)

  get("/") {
    contentType = "text/html"
    ssp("index", "body" -> "")
  }

  get("/classes/:date") {
    new AsyncResult { val is =
      classesRetrieverActor ? Process(params("date"))
    }
  }
}
