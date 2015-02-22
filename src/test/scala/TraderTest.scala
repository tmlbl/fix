import org.scalatest._

class TraderTest extends FunSpec with Matchers {

  describe("Trader object") {
    val testEvent = new Loader("src/test/data/events.csv").eventData(0)

    it("Should create the trader object") {
      new Trader(testEvent).run() should equal(0.0)
    }

  }

}
