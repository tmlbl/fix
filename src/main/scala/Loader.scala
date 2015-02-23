import io.Source._
import java.util.Date
import javax.xml.bind.DatatypeConverter

case class ForexEvent(title: String, impact: String, time: Date, country: String)

class Loader(filepath: String) {
  val src = fromFile(filepath)
  val iter = src.getLines()
  println("CSV file has header", iter.next())

  val eventData: List[ForexEvent] = iter.toList.map((line: String) => {
    val parsed = line.split(",")
    ForexEvent(
      title = parsed(0),
      impact = parsed(1),
      time = parseDate(parsed(2)),
      country = parsed(3)
    )
  })

  def parseDate(dts: String): Date = DatatypeConverter.parseDateTime(dts).getTime
}
