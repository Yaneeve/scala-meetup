package wordcount.future.concurrent

import java.util.concurrent.Executors

import com.typesafe.scalalogging.LazyLogging
import wordcount.Text

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.concurrent.Await.result
import scala.concurrent.duration._
import cats.Semigroup
import cats.implicits._
import wordcount.alg.Alg

import scala.language.postfixOps

object ConcurrentCalc extends App with Alg with LazyLogging {

  //  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(200))

  val start = System.nanoTime()

  val texts: (Array[String], Array[String]) = Text.aTaleOfTwoCities.splitAt(Text.aTaleOfTwoCities.length / 2)

  val wordCount1 = Future(mapReduce(texts._1, Some("1")))

  val wordCount2 = Future(mapReduce(texts._2, Some("2")))


  implicit val intAdditionSemigroup: Semigroup[Int] = (x: Int, y: Int) => x + y


  val eventualWordCount: Future[Map[String, Int]] = (wordCount1, wordCount2).mapN(_ |+| _)
  val max: Future[(String, Int)] = eventualWordCount.map(max(_))


  eventualWordCount.map(_.toSeq).foreach(_.foreach(tup => logger.debug(tup.toString)))

  max.foreach { m =>
    val end = System.nanoTime()
    logger.info("---> and the winner is: " + m + " <--- calc time is :" + (end - start) / 1000000.0)
  }

  result(max, 15 seconds)

}
