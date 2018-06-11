package wordcount.sequential


import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import wordcount.Text

import scala.concurrent.ExecutionContext.Implicits.global


object SeqCalc extends App with LazyLogging {

  val start = System.nanoTime()

  val texts: (Array[String], Array[String]) = Text.aTaleOfTwoCities.splitAt(Text.aTaleOfTwoCities.length/2)

  val map1 = IO {
    logger.info(s"HAL 1 here, just to inform you that I will take care of ${texts._1.head} ...")
    texts._1.groupBy(identity).map { case (key, arr) => (key, arr.length) }
  }

  val map2 = IO {
    logger.info(s"HAL 2 here, just to inform you that I will take care of ${texts._2.head} ...")
    texts._2.groupBy(identity).map { case (key, arr) => (key, arr.length) }
  }

  import cats.Semigroup
  import cats.implicits._

  implicit val intAdditionSemigroup: Semigroup[Int] = (x: Int, y: Int) => x + y


  private val eventualMapReduce: IO[Map[String, Int]] = (map1, map2).mapN(_ |+| _)

  private val max: IO[(String, Int)] = eventualMapReduce.map(_.maxBy(_._2))
  val end = System.nanoTime()
  val print: IO[Seq[Unit]] = eventualMapReduce.map(_.toSeq).map(_.map(tup => logger.debug(tup.toString)))
  val winner: IO[Unit] = max.map(m =>
  logger.info("---> and the winner is: " + m + " <--- calc time is :" +  (end - start)/1000000.0))

  val prog = for {
    _ <- print
    _ <- winner
  } yield ()

  prog.unsafeRunSync()


}
