package wordcount.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import cats.Semigroup
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import wordcount.Text
import wordcount.akka.ConcurrentRouterCalc.system

object ActorCalc extends App with LazyLogging {

  val start = System.nanoTime()

  case class PayLoad(text: Array[String])
  case class Count(words: Map[String, Int])
  case class Next(actorRef: ActorRef)

  class Mapper extends Actor with LazyLogging {
    override def receive: Receive = {
      case Next(actorRef) => context.become(active(actorRef))
    }

    def active(actorRef: ActorRef): Actor.Receive = {
      case PayLoad(text) =>
        logger.info(s"HAL ${self.path} here, just to inform you that I will take care of ${text.head} ...")
        val words = text.groupBy(identity).map { case (key, arr) => (key, arr.length) }
        actorRef ! Count(words)
    }
  }


  class MaxLocator extends Actor with LazyLogging {


    implicit val intAdditionSemigroup: Semigroup[Int] = (x: Int, y: Int) => x + y

    override def receive: Receive = {
      case Count(words) =>
        context.become(getOne(words))
    }

    def getOne(words: Map[String, Int]): Actor.Receive = {
      case Count(moreWords) =>
        val mapReduce = (words, moreWords).mapN(_ |+| _)
        val max = mapReduce.maxBy(_._2)

        val end = System.nanoTime()

        mapReduce.toSeq.foreach(tup => logger.debug(tup.toString))
        logger.info("---> and the winner is: " + max + " <--- calc time is :" + (end - start) / 1000000.0)

        system.terminate()
    }
  }

  val system = ActorSystem("word-count")


  val mapper = system.actorOf(Props[Mapper], "mapper")
  val maxLocator = system.actorOf(Props[MaxLocator], "max")

  mapper ! Next(maxLocator)
  val texts: (Array[String], Array[String]) = Text.aTaleOfTwoCities.splitAt(Text.aTaleOfTwoCities.length / 2)
  mapper ! PayLoad(texts._1)
  mapper ! PayLoad(texts._2)

}