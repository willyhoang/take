import javax.servlet.ServletContext

import io.github.willyhoang.take.TakeScalatraServlet
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new TakeScalatraServlet, "/*")
  }
}
