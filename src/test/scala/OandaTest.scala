import org.scalatest._
import java.util.Date

class OandaTest extends FunSpec with Matchers {

  describe("Oanda object") {

    it("Should get some candles") {
      val start = new Date(new Date().getTime - 1000 * 60 * 5)
      val response = Oanda.fetchCandles(
        instrument = "EUR_USD",
        start = start,
        end = new Date()
      )
      response.candles foreach((c: Candle) => {
        c.date.getTime
      })
    }

  }

}
