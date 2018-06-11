package wordcount.concurrent

import java.util.concurrent.Executors

import com.typesafe.scalalogging.LazyLogging
import wordcount.Text

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object Calc extends App with LazyLogging {

//  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(200))

  val start = System.nanoTime()

  val texts: (Array[String], Array[String]) = Text.aTaleOfTwoCities.splitAt(Text.aTaleOfTwoCities.length/2)

  val map1 = Future {
    logger.info(s"HAL 1 here, just to inform you that I will take care of ${texts._1.head} ...")
    texts._1.groupBy(identity).map { case (key, arr) => (key, arr.length) }
  }

  val map2 = Future {
    logger.info(s"HAL 2 here, just to inform you that I will take care of ${texts._2.head} ...")
    texts._2.groupBy(identity).map { case (key, arr) => (key, arr.length) }
  }

  import cats.Semigroup
  import cats.implicits._

  implicit val intAdditionSemigroup: Semigroup[Int] = (x: Int, y: Int) => x + y


  private val eventualMapReduce: Future[Map[String, Int]] = (map1, map2).mapN(_ |+| _)

  private val max: Future[(String, Int)] = eventualMapReduce.map(_.maxBy(_._2))
  val end = System.nanoTime()
  eventualMapReduce.map(_.toSeq).foreach(_.foreach(tup => logger.debug(tup.toString)))
  max.foreach(m =>
  logger.info("---> and the winner is: " + m + " <--- calc time is :" +  (end - start)/1000000.0))

  import scala.concurrent.Await.result
  import scala.concurrent.duration._
  result(max, 15 seconds)

}
