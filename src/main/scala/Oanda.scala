import java.text.SimpleDateFormat
import java.util.Date
import System.out.println

import scalaj.http.Http
import spray.json._

case class Candle(time: Date, openMid: Long, highMid: Long, lowMid: Long,
                  closeMid: Long, volume: Int, complete: Boolean)

case class CandleResponse(instrument: String, granularity: String, candles: Seq[Candle])

object Oanda {
  val baseUrl = "http://api-sandbox.oanda.com"
  val rfcDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")

  implicit val DateFormat = new RootJsonFormat[Date] {
    def read(json: JsValue): Date = {
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(json.compactPrint.toString)
    }
    def write(date: Date) = JsString(
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(date)
    )
  }

  implicit val CandleFormat = new RootJsonFormat[Candle] {
    def read(json: JsValue): Candle = {
      println("Candle data class name is", json.getClass.toString)
      println(json.compactPrint)
      val fields = json.asJsObject.getFields("time")
      fields match {
        case Seq(JsString(time)) => new Candle(time = rfcDate.parse(time), 0, 0, 0, 0, 0, false)
        case _ => throw new DeserializationException("Match failed")
      }
    }
    def write(candle: Candle) = JsObject(
      "time" -> JsString(candle.time.toString),
      "openMid" -> JsNumber(candle.openMid),
      "highMid" -> JsNumber(candle.highMid),
      "lowMid" -> JsNumber(candle.lowMid),
      "closeMid" -> JsNumber(candle.closeMid),
      "volume" -> JsNumber(candle.volume),
      "complete" -> JsBoolean(candle.complete)
    )
  }

  implicit val SeqCandleFormat = new RootJsonFormat[Seq[Candle]] {
    def read(json: JsValue): Seq[Candle] = {
      json.asJsObject.getFields().map((field: JsValue) => {
        println(field)
        field.convertTo[Candle]
      })
    }
    def write(candles: Seq[Candle]): JsValue = candles.toJson
  }

  def getCandles(instrument: String, start: Date, end: Date): Unit = {
    val res = Http(baseUrl + "/v1/candles").params(Seq(
      "instrument" -> instrument,
      "start" -> rfcDate.format(start),
      "end" -> rfcDate.format(end),
      "candleFormat" -> "midpoint"
    ))
    val resJson = res.asString.body.parseJson
    val candles = resJson.asJsObject.getFields("candles")
    candles.foreach((candleList: JsValue) => candleList.convertTo[Seq[Candle]])
  }
}
