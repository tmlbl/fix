
import java.util.Date

import breeze.linalg._

class Trader(event: ForexEvent) {
  println("Created trader for event", event.title)

  def run(): Double = {
    println(s"Simulating trade on event ${event.title} at ${event.time.toString}")
    println("Getting data...")
    val response = Oanda.fetchCandles(
      instrument = "EUR_USD",
      start = new Date(event.time.getTime - 1000 * 60 * 60),
      end = event.time
    )
    val pricesSeq = response.candles map(_.highMid)
    val prices = DenseVector.zeros[Double](pricesSeq.length)
    for((x, i) <- prices.activeIterator) prices.update(x, pricesSeq(x))
    println(prices)
    40
  }

}
