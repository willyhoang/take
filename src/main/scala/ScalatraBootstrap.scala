import javax.servlet.ServletContext

import _root_.akka.actor.{ActorSystem, Props}
import io.github.willyhoang.take.{Reload, ClassesRetrieverActor, ProcessMultiple, TakeScalatraServlet}
import org.scalatra._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class ScalatraBootstrap extends LifeCycle {

  val system = ActorSystem()
  val myActor = system.actorOf(Props(new ClassesRetrieverActor(system)))

  val dailyRefresh = system.scheduler.schedule(0 seconds, 1 days, myActor, ProcessMultiple())
  val bihourlyRefresh = system.scheduler.schedule(2 hours, 2 hours, myActor, Reload())

  override def init(context: ServletContext) {
    context.mount(new TakeScalatraServlet(system, myActor), "/*")
  }
}
