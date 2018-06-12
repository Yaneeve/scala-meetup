package errors.sequential

import com.typesafe.scalalogging.LazyLogging

import scala.util.Try

object WithOption extends App with LazyLogging {

  case object ShouldntDoThat extends Exception

  def divide(dividend: Int, divisor: Int): Option[Float] = {
    if (divisor == 0 ) None
    else Some(dividend.toFloat / divisor)
  }

  def compute(a: Int, b: Int): Option[Float] = {
    divide(a, b).map { f =>
      logger.info("I divide now I conquer")
      f
    }
  }

  def calculate(a: Int, b: Int, c: Int): String = {
    compute(a, b + c).fold({
        logger.warn("tsk tsk")
        "default return value"
    })(_.toString)
  }

  logger.info(calculate(1, 2, 3)) // pass
  logger.info(calculate(1, -1, 1)) // fail
}
