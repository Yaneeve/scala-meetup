package wordcount.akka

import akka.actor.Props
import akka.routing.{Broadcast, RoundRobinPool}
import wordcount.Text

object ConcurrentRouterCalc extends App {

  import ActorCalcModule._

  val router = system.actorOf(RoundRobinPool(2).props(Props[Mapper]), "round-robin")

  val maxLocator = system.actorOf(Props[MaxLocator], "max")

  maxLocator ! Start(System.nanoTime())
  router ! Broadcast(Next(maxLocator))
  val texts: (Array[String], Array[String]) = Text.aTaleOfTwoCities.splitAt(Text.aTaleOfTwoCities.length / 2)
  router ! PayLoad(texts._1)
  router ! PayLoad(texts._2)

  shutdown()
}
