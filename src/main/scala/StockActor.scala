import akka.actor.{Actor, ActorLogging, ActorRef}
import scalaj.http.Http

import scala.concurrent.ExecutionContext

case class GetStockData(str: String)
case class NotifyMembers(str: String, value: Double)
case class SaveStockData(str: String, value: Double)

class StockActor(notifyActor: ActorRef)(implicit val ec: ExecutionContext) extends Actor with ActorLogging {

  //global state within the actor is still safe; not that I approve of :P
  var stockRates: Map[String, List[Int]] =  Map()

  override def receive = {

    case GetStockData(stock) => {
      val uri = s"https://api.iextrading.com/1.0/stock/${stock}/price"
      val res = Http(uri).asString.body

      self ! SaveStockData(stock, res.toDouble)
    }

    case SaveStockData(stock, value) => {

      val values: Option[List[Int]] = stockRates.get(stock)
      val stockValue = values match {
        case Some(stocks: List[Int]) => value :: stocks
        case None => List(value)
      }

      stockRates + (stock -> stockValue)
      notifyActor ! Notify(stock, value)
    }

  }
}

case class Notify(stockName: String, value: Double)

class NotifyActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case Notify(stock: String, value: Double) =>
      println(s"$stock has the highest value of $value")
  }
}