package wordcount.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import cats.Semigroup
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import wordcount.alg.Alg

object ActorCalcModule extends Alg with LazyLogging {



  case class PayLoad(text: Array[String])
  case class Count(words: Map[String, Int])
  case class Next(actorRef: ActorRef)
  case class Start(start: Long)

  class Mapper extends Actor {

    override def receive: Receive = {
      case Next(actorRef) => context.become(active(actorRef))
    }

    def active(actorRef: ActorRef): Actor.Receive = {
      case PayLoad(text) =>
        val words = mapReduce(text, Some(self.path.toString))
        actorRef ! Count(words)
    }
  }


  class MaxLocator extends Actor {

    implicit val intAdditionSemigroup: Semigroup[Int] = (x: Int, y: Int) => x + y

    override def receive: Receive = {
      case Start(start) => context.become(active(start))
    }

    def active(start: Long): Actor.Receive = {
      case Count(words) =>
        context.become(getOne(words, start))
    }

    def getOne(words: Map[String, Int], start: Long): Actor.Receive = {
      case Count(moreWords) =>
        val mapReduce = (words, moreWords).mapN(_ |+| _)
        val m = max(mapReduce)

        val end = System.nanoTime()

        mapReduce.toSeq.foreach(tup => logger.debug(s"{${self.path}} ${tup.toString}"))
        logger.info(s"{${self.path}} ---> and the winner is: " + m + " <--- calc time is :" + (end - start) / 1000000.0)

        system.terminate()
    }
  }

  val system = ActorSystem("word-count")


}