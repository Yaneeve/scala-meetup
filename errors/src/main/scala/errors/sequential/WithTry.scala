package errors.sequential

import com.typesafe.scalalogging.LazyLogging

import scala.util.Try

object WithTry extends App with LazyLogging {

  case object ShouldntDoThat extends Exception

  def divide(dividend: Int, divisor: Int): Try[Float] = Try {
    if (divisor == 0 ) throw ShouldntDoThat //new RuntimeException("didn't see that one coming")
    else dividend.toFloat / divisor
  }

  def compute(a: Int, b: Int): Try[Float] = {
    divide(a, b).map { f =>
      logger.info("I divide now I conquer")
      f
    }
  }

  def calculate(a: Int, b: Int, c: Int): String = {
    compute(a, b + c).fold({
      case ShouldntDoThat =>
        logger.warn("tsk tsk")
        "default return value"
      case t => throw t // TODO comment out during pres
    }, _.toString)
  }

  logger.info(calculate(1, 2, 3)) // pass
  logger.info(calculate(1, -1, 1)) // fail
}
