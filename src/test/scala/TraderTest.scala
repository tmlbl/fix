import org.scalatest._

class TraderTest extends FunSpec with Matchers {
  describe("Trader object") {
    it("Should create the trader object") {
      new Trader().run() should equal(0.0)
    }
  }
}
