import org.scalatest._

class TraderTest extends FunSpec with Matchers {

  describe("Trader object") {
    val eventData = new Loader("src/test/data/events.csv").eventData
    val testEvent = eventData(0)

    it("Should create the trader object") {
      new Trader(testEvent).run() should equal(40)
    }

    it("Should run several simulations") {
      eventData foreach((e: ForexEvent) => {
        new Trader(e).run()
      })
    }

  }

}
