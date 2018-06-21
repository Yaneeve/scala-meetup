package wordcount.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import cats.Semigroup
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import wordcount.Text

object ConcurrentManualDispatcherCalc extends App {

  import ActorCalcModule._

  val mapper1 = system.actorOf(Props[Mapper].withDispatcher("word-count-dispatcher"), "mapper1")
  val mapper2 = system.actorOf(Props[Mapper].withDispatcher("word-count-dispatcher"), "mapper2")

  val maxLocator = system.actorOf(Props[MaxLocator].withDispatcher("word-count-dispatcher"), "max")

  maxLocator ! Start(System.nanoTime())
  mapper1 ! Next(maxLocator)
  mapper2 ! Next(maxLocator)
  val texts: (Array[String], Array[String]) = Text.aTaleOfTwoCities.splitAt(Text.aTaleOfTwoCities.length / 2)
  mapper1 ! PayLoad(texts._1)
  mapper2 ! PayLoad(texts._2)

  shutdown()
}
