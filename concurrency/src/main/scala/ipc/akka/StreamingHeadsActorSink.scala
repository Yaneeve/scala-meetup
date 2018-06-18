package ipc.akka

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import ipc.Text

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

object StreamingHeadsActorSink extends App {

  implicit val system = ActorSystem("streaming-heads")
  implicit val materializer = ActorMaterializer()

  val adam: Source[String, NotUsed] = Source.apply[String](Text.adam.to[scala.collection.immutable.Iterable])
  val beatrix: Source[String, NotUsed] = Source.apply[String](Text.beatrix.to[scala.collection.immutable.Iterable])

  val zipped: Source[(String, String), NotUsed] = adam zip beatrix

  case object Ack

  case object Init

  case object Complete

  class Printer extends Actor with ActorLogging {
    override def receive: Receive = {
      case (a: String, b: String) =>
        log.info(s"Adam said: $a")
        log.info(s"Btrx said: $b")
        sender() ! Ack
      case Init =>
        log.debug("begin")
        sender() ! Ack
      case Complete =>
        log.debug("done")
      case some =>
        log.warning("else " + some.toString)
        sender() ! Ack
    }
  }

  val sink = Sink.actorRefWithAck(system.actorOf(Props[Printer], "printer"), Init, Ack, Complete)
  zipped.runWith(sink)

  val terminate: Runnable = () => system.terminate()
  system.scheduler.scheduleOnce(5 seconds, terminate)

}
