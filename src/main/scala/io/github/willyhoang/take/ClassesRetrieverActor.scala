package io.github.willyhoang.take

import _root_.akka.pattern.ask
import akka.actor.{Actor, ActorSystem}
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import io.github.willyhoang.take.scrapers.{BDCScraperUtil, EXPGScraperUtil, PeridanceScraperUtil}
import net.liftweb.json.Serialization.write
import net.liftweb.json.ext.JodaTimeSerializers
import net.liftweb.json.{NoTypeHints, Serialization}
import org.joda.time.LocalDate
import org.scalatra.guavaCache.GuavaCache

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

case class Process(date: String)
case class ProcessMultiple()

class ClassesRetrieverActor(system: ActorSystem) extends Actor with LazyLogging {
  val cacheBackend = GuavaCache
  val expirationTime = Some(Duration(3, HOURS))
  implicit val defaultTimeout = Timeout(1000 * 300)

  def getCachedClasses(date: String): Future[String] = {
    cacheBackend.get[String](date) match {
      case Some(v) => {
        logger.info(s"Loading ${date} value from cache.")
        Future.successful(v)
      }
      case None => {
        logger.info(s"Cold miss. Fetching ${date} value.")
        getClasses(date)
      }
    }
  }

  def getClasses(date: String): Future[String] = {
    LocalDate.parse(date)
    val bdcClasses = BDCScraperUtil.getClasses(date)
    val expgClasses = EXPGScraperUtil.getClasses(date)
    val peridanceClasses = PeridanceScraperUtil.getClasses(date)

    for {
      bdc <- bdcClasses
      expg <- expgClasses
      peridance <- peridanceClasses
    } yield {
      val sortedClasses = (bdc ++ expg ++ peridance).sortBy(_.startTime.toDateTimeToday.getMillis)
      implicit val formats = Serialization.formats(NoTypeHints) ++ JodaTimeSerializers.all
      val jsonClasses = write(sortedClasses)
      // Save result in cache
      cacheBackend.put(date, jsonClasses, expirationTime)
      jsonClasses
    }
  }

  def warmCacheWithClasses() = {
    val today = new LocalDate()
    val dates : List[String] = List.range(0, 6).map(today.plusDays(_).toString("yyyy-MM-dd"))
    logger.info(s"Warming caches for dates: ${dates}")

    for (date <- dates) {
      getClasses(date)
    }
  }

  def receive = {
    case Process(date) => sender ? getCachedClasses(date)
    case ProcessMultiple() => {
      sender ! warmCacheWithClasses()
    }
  }
}
