package wordcount.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.routing.{Broadcast, RoundRobinPool}
import cats.Semigroup
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import wordcount.Text

object ConcurrentRouterDispatcherCalc extends App with LazyLogging {

  val start = System.nanoTime()

  case class PayLoad(text: Array[String])
  case class Count(words: Map[String, Int])
  case class Next(actorRef: ActorRef)

  class Mapper extends Actor {
    override def receive: Receive = {
      case Next(actorRef) => context.become(active(actorRef))
    }

    def active(actorRef: ActorRef): Actor.Receive = {
      case PayLoad(text) =>
        logger.info(s"HAL ${self.path} here, just to inform you that I will take care of ${text.head} ...")
        val words = text.groupBy(identity).map { case (key, arr) => (key, arr.length) }
        cpuIntensive
        actorRef ! Count(words)
    }
  }


  class MaxLocator extends Actor  {


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

  val router = system.actorOf(RoundRobinPool(2).props(Props[Mapper]).withDispatcher("word-count-dispatcher"), "round-robin")

  val maxLocator = system.actorOf(Props[MaxLocator].withDispatcher("word-count-dispatcher"), "max")

  router ! Broadcast(Next(maxLocator))
  val texts: (Array[String], Array[String]) = Text.aTaleOfTwoCities.splitAt(Text.aTaleOfTwoCities.length / 2)
  router ! PayLoad(texts._1)
  router ! PayLoad(texts._2)


  def cpuIntensive = 1 to 10 foreach {_ => ((1 to Int.MaxValue)) foreach { i => Math.sqrt(i.toDouble) }}

}
