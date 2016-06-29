import javax.servlet.ServletContext

import _root_.akka.actor.{ActorSystem, Props}
import io.github.willyhoang.take.{MyActor, ProcessMultiple, TakeScalatraServlet}
import org.scalatra._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ScalatraBootstrap extends LifeCycle {

  val system = ActorSystem()
  val myActor = system.actorOf(Props(new MyActor(system)))

  val tick = system.scheduler.schedule(0 seconds, 3 hours, myActor, ProcessMultiple())

  override def init(context: ServletContext) {
    context.mount(new TakeScalatraServlet(system, myActor), "/*")
  }
}
