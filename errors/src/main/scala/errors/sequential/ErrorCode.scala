package errors.sequential

import com.typesafe.scalalogging.LazyLogging
import errors.sequential.WithOption.logger

object ErrorCode extends App with LazyLogging {

  def divide(dividend: Int, divisor: Int): Float = {
    if (divisor == 0 ) Float.PositiveInfinity
    else dividend.toFloat / divisor
  }

  def compute(a: Int, b: Int): Float = {
    val ret = divide(a, b)
    if (ret != Float.PositiveInfinity) logger.info("I divide now I conquer")
    ret
  }

  def calculate(a: Int, b: Int, c: Int): String = {
    compute(a, b + c).toString
  }

  logger.info(calculate(1, 2, 3)) // pass
  logger.info(calculate(1, -1, 1)) // fail
}
