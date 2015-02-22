import org.scalatest._
import java.util.Date

class OandaTest extends FunSpec with Matchers {

  describe("Oanda object") {

    it("Should get some candles") {
      val start = new Date(new Date().getTime - 1000 * 60 * 5)
      val candles = Oanda.getCandles(
        instrument = "EUR_USD",
        start = start,
        end = new Date()
      )
    }

  }

}
