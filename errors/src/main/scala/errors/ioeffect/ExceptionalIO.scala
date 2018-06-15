package errors.ioeffect

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Await.result
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

import cats.effect.IO


object ExceptionalIO extends App with LazyLogging {

  case object ShouldntDoThat extends Exception

  def divide(dividend: Int, divisor: Int): IO[Float] =
    if (divisor == 0 )
      IO.fromEither(Left(ShouldntDoThat))
//      IO.raiseError(ShouldntDoThat)
    ///throw ShouldntDoThat
    else IO {dividend.toFloat / divisor
  }

  def compute(a: Int, b: Int): IO[Float] = for {
  f <- divide(a, b)
  _ <- IO(logger.info("I divide now I conquer"))
  } yield f

  def calculate(a: Int, b: Int, c: Int): IO[String] = {
      compute(a, b + c).map(_.toString)
  }



  (for {
    calc1 <- calculate(1, 2, 3) //pass
    calc2 <- calculate(1, -1, 1) //fail
  } yield {
    logger.info(calc1)
    logger.info(calc2)
  }).unsafeRunSync()

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
