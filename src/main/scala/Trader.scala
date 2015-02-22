import com.cra.figaro.language._
import com.cra.figaro.algorithm.sampling._

class Trader {

  def run(): Double = {
    val hw = Constant("Hello, world!")
    val alg = Importance(1000, hw)
    alg.start()
    alg.probability(hw, "Hollow, world!")
  }

}
