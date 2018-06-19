package wordcount.ioeffect.concurrent

import java.util.concurrent.Executors

import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import wordcount.Text

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import cats.Semigroup
import cats.implicits._
import wordcount.alg.Alg
import scala.concurrent.ExecutionContext.Implicits.global

object ParMapNCalc extends App with Alg with LazyLogging {

//  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(200))

  val start = System.nanoTime()

  val texts: (Array[String], Array[String]) = Text.aTaleOfTwoCities.splitAt(Text.aTaleOfTwoCities.length/2)

  val wordCount1 = IO(mapReduce(texts._1, Some("1")))

  val wordCount2 = IO(mapReduce(texts._2, Some("2")))

  implicit val intAdditionSemigroup: Semigroup[Int] = (x: Int, y: Int) => x + y

  val eventualWordCount: IO[Map[String, Int]] = (wordCount1, wordCount2).parMapN(_ |+| _)

  val max: IO[(String, Int)] = eventualWordCount.map(max(_))

  val end = System.nanoTime()

  val print: IO[Seq[Unit]] = eventualWordCount.map(_.toSeq).map(_.map(tup => logger.debug(tup.toString)))
  val winner: IO[Unit] = max.map(m =>
  logger.info("---> and the winner is: " + m + " <--- calc time is :" +  (end - start)/1000000.0))

  val prog = for {
    _ <- print
    _ <- winner
  } yield ()

  prog.unsafeRunSync()

}
