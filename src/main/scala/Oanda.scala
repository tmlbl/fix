import java.util.Date
import System.out.println

import scalaj.http.Http
import net.liftweb.json._

case class Candle(time: String, openMid: Double, highMid: Double, lowMid: Double,
                  closeMid: Double, volume: Int, complete: Boolean)
{
  val date = new Date(time.toLong / 1000)
}

case class CandleResponse(instrument: String, granularity: String, candles: Seq[Candle])

object Oanda {
  val baseUrl = "http://api-sandbox.oanda.com"
  def nixDate(date: Date): String = { (date.getTime / 1000).toInt.toString }
  implicit val formats = DefaultFormats

  def fetchCandles(instrument: String, start: Date, end: Date): CandleResponse = {
    val res = Http(baseUrl + "/v1/candles").params(Seq(
      "instrument" -> instrument,
      "start" -> nixDate(start),
      "end" -> nixDate(end),
      "candleFormat" -> "midpoint",
      "granularity" -> "M1"
    )).header("Content-Type", "application/json").header("X-Accept-Datetime-Format", "UNIX")
    println(res.asString.body)
    parse(res.asString.body).extract[CandleResponse]
  }

}
