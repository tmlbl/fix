import System.out.println

object Fix {

  def main(args: Array[String]): Unit = {
    args.last match {
      case "events" => weightEvents()
      case bs => println("Unrecognized command: " + bs)
    }
  }

  def weightEvents(): Unit = {
    println("Beginning the events test sequence")
    val loader = new Loader("src/test/data/events.csv")
  }

}
