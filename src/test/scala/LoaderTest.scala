import java.util.Date

import org.scalatest._

class LoaderTest extends FunSpec with Matchers {

  describe("Loader object") {
    val csvpath = "src/test/data/events.csv"

    it("Should parse an ISODate") {
      val isodate = "2015-01-30T08:00:00Z"
      new Loader(csvpath).parseDate(isodate).before(new Date()) should equal(true)
    }

    it("Should load the events data") {
      val loader = new Loader(csvpath)
      loader.eventData(0).time.getTime
    }

  }

}
