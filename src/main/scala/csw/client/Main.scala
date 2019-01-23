package csw.client

import akka.actor.CoordinatedShutdown
import ammonite.util.Res

object Main {

  def main(args: Array[String]): Unit = {
    val wiring = new Wiring()
    import wiring._
    import frameworkWiring._

    val (result: Res[Any], paths) = ammonite
      .Main(
        predefCode = """
                |import scala.concurrent.duration.Duration
                |import scala.concurrent.duration.Duration
                |import akka.util.Timeout
                |import scala.concurrent.duration.DurationDouble
                |import scala.concurrent.{Await, Future}
                |import csw.params.core.generics.KeyType._
                |import csw.params.events.SystemEvent
                |import csw.params.events.EventName
                |import csw.params.events.EventKey
                |import csw.params.commands._
                |import csw.params.core.models._
                |implicit class RichFuture[T](val f: Future[T]) {
                |  def get: T = Await.result(f, Duration.Inf)
                |}
                |implicit val mat = materializer
                |implicit val timeout: Timeout = Timeout(10.seconds)
                |""".stripMargin
      )
      .run(
        "componentFactory" -> componentFactory,
        "locationService"  -> locationService,
        "eventService"     -> eventService,
        "subscriber"       -> eventService.defaultSubscriber,
        "publisher"        -> eventService.defaultPublisher,
        "materializer"     -> materializer
      )

    CoordinatedShutdown(frameworkSystem).run(CoordinatedShutdown.JvmExitReason)
  }
}
