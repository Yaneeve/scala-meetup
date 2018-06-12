package errors.sequential

import com.typesafe.scalalogging.LazyLogging

import scala.util.Try

object WithEither extends App with LazyLogging {

  sealed trait OhMan extends Exception
  case object ShouldntDoThat extends OhMan

  def divide(dividend: Int, divisor: Int): Either[OhMan, Float] =
    if (divisor == 0 ) Left(ShouldntDoThat)
    else Right(dividend.toFloat / divisor)


  def compute(a: Int, b: Int): Either[OhMan, Float] = {
    divide(a, b).map { f =>
      logger.info("I divide now I conquer")
      f
    }
  }

  def calculate(a: Int, b: Int, c: Int): String = {
    compute(a, b + c) fold ({
      case ShouldntDoThat =>
        logger.warn("tsk tsk")
        "default return value"
    },
    _.toString)

  }

  logger.info(calculate(1, 2, 3)) // pass
  logger.info(calculate(1, -1, 1)) // fail
}
