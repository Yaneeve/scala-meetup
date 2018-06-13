package errors.sequential

import com.typesafe.scalalogging.LazyLogging

object Exceptional extends App with LazyLogging {

  case object ShouldntDoThat extends Exception

  def divide(dividend: Int, divisor: Int): Float = {
    if (divisor == 0 ) throw ShouldntDoThat // new RuntimeException("didn't see that coming")
    else dividend.toFloat / divisor
  }

  def compute(a: Int, b: Int): Float = {
    val ret = divide(a, b)
    logger.info("I divide now I conquer")
    ret
  }

  def calculate(a: Int, b: Int, c: Int): String = {
    try {
      compute(a, b + c).toString
    } catch {
      case ShouldntDoThat => logger.warn("tsk tsk");  "default return value"
    } finally {
      //"finally it happened to me"
    }
  }

  logger.info(calculate(1, 2, 3)) // pass
  logger.info(calculate(1, -1, 1)) // fail
}
