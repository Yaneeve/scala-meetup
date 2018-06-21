package wordcount.akka

import akka.actor.Props
import akka.routing.{Broadcast, RoundRobinPool}
import wordcount.Text

object ConcurrentRouterDispatcherCalc extends App  {

  import ActorCalcModule._

  val router = system.actorOf(RoundRobinPool(2).props(Props[Mapper]).withDispatcher("word-count-dispatcher"), "round-robin")

  val maxLocator = system.actorOf(Props[MaxLocator].withDispatcher("word-count-dispatcher"), "max")

  maxLocator ! Start(System.nanoTime())
  router ! Broadcast(Next(maxLocator))
  val texts: (Array[String], Array[String]) = Text.aTaleOfTwoCities.splitAt(Text.aTaleOfTwoCities.length / 2)
  router ! PayLoad(texts._1)
  router ! PayLoad(texts._2)

  shutdown()
}
