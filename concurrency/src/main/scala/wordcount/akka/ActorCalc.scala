package wordcount.akka

import akka.actor.Props
import wordcount.Text

object ActorCalc extends App {

  import ActorCalcModule._

  val mapper = system.actorOf(Props[Mapper], "mapper")
  val maxLocator = system.actorOf(Props[MaxLocator], "max")

  maxLocator ! Start(System.nanoTime())
  mapper ! Next(maxLocator)
  val texts: (Array[String], Array[String]) = Text.aTaleOfTwoCities.splitAt(Text.aTaleOfTwoCities.length / 2)
  mapper ! PayLoad(texts._1)
  mapper ! PayLoad(texts._2)

  shutdown()

}
