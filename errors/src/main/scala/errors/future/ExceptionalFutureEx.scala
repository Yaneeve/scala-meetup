package errors.future

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Await.result
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps


object ExceptionalFutureEx extends App with LazyLogging {

  case object ShouldntDoThat extends Exception

  def divide(dividend: Int, divisor: Int): Future[Float] = Future {
    if (divisor == 0 ) throw ShouldntDoThat
    else dividend.toFloat / divisor
  }

  def compute(a: Int, b: Int): Future[Float] = {
    throw new RuntimeException("pre setup has failed") // TODO comment out abd go to the angel scenario
    divide(a, b).andThen{ case _ =>
    logger.info("I divide now I conquer")
//        throw new RuntimeException("an angel has lost its wings")
    }
  }

  def calculate(a: Int, b: Int, c: Int): Future[String] = {
//    try { // TODO comment in
      compute(a, b + c).map(_.toString)
//    } catch {
//      case _: Throwable => logger.warn("tsk tsk");  Future.successful("default return value")
//    } finally {
//      Future.successful("finally it happened to me")
//    }
  }


  logger.info(result(
    calculate(1, 2, 3)
  , 5 seconds))

  logger.info("I want to be printed")
}
