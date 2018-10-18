import akka.actor.{ActorSystem, Props}

import scala.concurrent.duration._

object Main extends App {

  implicit val system = ActorSystem("actor-system")
  implicit val executionContext = system.dispatcher

  val notifyActor = system.actorOf(Props[NotifyActor])

  system.scheduler.schedule(
    initialDelay = 10 milliseconds,
    interval = 20 second ,
    receiver = system.actorOf(Props(new StockActor(notifyActor)), name = "GOOGLStock"),
    message = GetStockData("GOOGL"))

  system.scheduler.schedule(
    initialDelay = 10 milliseconds,
    interval = 20 second,
    receiver = system.actorOf(Props(new StockActor(notifyActor)), name = "FBStock"),
    message = GetStockData("FB"))

  system.scheduler.schedule(
    initialDelay = 10 milliseconds,
    interval = 20 second,
    receiver = system.actorOf(Props(new StockActor(notifyActor)), name = "MSFTStock"),
    message = GetStockData("MSFT"))
}
