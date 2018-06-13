package errors.future

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

import scala.concurrent.Await.result
import scala.concurrent.duration._

import cats.implicits._


object ExceptionalFuture extends App with LazyLogging {

  case object ShouldntDoThat extends Exception

  def divide(dividend: Int, divisor: Int): Future[Float] = Future {
    if (divisor == 0 ) throw ShouldntDoThat // new RuntimeException("didn't see that coming")
    else dividend.toFloat / divisor
  }

  def compute(a: Int, b: Int): Future[Float] = {
    divide(a, b).andThen{ case _ =>
    logger.info("I divide now I conquer")}
  }

  def calculate(a: Int, b: Int, c: Int): Future[String] = {
      compute(a, b + c).map(_.toString)
  }


  result(
    for {
    calc1 <- calculate(1, 2, 3) //pass
    calc2 <- calculate(1, -1, 1) //fail
  } yield {
    logger.info(calc1)
    logger.info(calc2)
  }, 5 seconds)

//  result(
//  (calculate(1, 2, 3), //pass
////    Future.successful("hello"))
//      calculate(1, -1, 1)) //fail
//  mapN{case (calc1, calc2) =>
//    logger.info(calc1)
//    logger.info(calc2)
//  }, 5 seconds)

  logger.info("I want to be printed")
}
