
import java.util.Date

import breeze.linalg._

class Trader(event: ForexEvent) {
  val breadth = 1000 * 60 * 60 // One hour before and after
  val symbol = getTradeableSymbol(event.country)

  def run(): Double = {
    println(s"Simulating trade on event ${event.title} at ${event.time.toString}")
    println("Getting data...")
    val eventTime = event.time.getTime
    val beforeTime = eventTime - breadth
    val afterTime = eventTime + breadth
    val response = Oanda.fetchCandles(
      instrument = symbol,
      start = new Date(beforeTime),
      end = new Date(afterTime)
    )
    val pricesSeq = response.candles map(_.highMid)
    val timestampSeq = response.candles.map(_.date.getTime)
    val prices = DenseVector.zeros[(Double, Double)](pricesSeq.length)
    for((x, i) <- prices.activeIterator) prices.update(x, (pricesSeq(x), timestampSeq(x)))
    println(s"Price difference is ${getPriceMove(event, prices)}")
    40
  }

  def getTradeableSymbol(country: String): String = {
    country.toString match {
      case "EUR" => "EUR_USD"
      case "USD" => "EUR_USD"
      case "CAD" => "USD_CAD"
      case "JPY" => "USD_JPY"
      case "GBP" => "GBP_USD"
      case "AUD" => "AUD_USD"
      case "CHF" => "USD_CHF"
      case other =>
        println(s"WARNING: No tradeable symbol for $country")
        "EUR_USD"
    }
  }

  def getPriceMove(event: ForexEvent, data: DenseVector[(Double, Double)]): Double = {
    val beforePrices = new scala.collection.mutable.ArrayBuffer[Double]
    val afterPrices = new scala.collection.mutable.ArrayBuffer[Double]
    data foreach((datum: (Double, Double)) => {
      if (datum._2 < event.time.getTime) {
        beforePrices += datum._1
      } else {
        afterPrices += datum._1
      }
    })
    val beforeMean = beforePrices.sum / beforePrices.length
    val afterMean = afterPrices.sum / afterPrices.length
    Math.abs(beforeMean - afterMean)
  }

}
