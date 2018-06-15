package ipc.akka

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import ipc.Text

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

object TalkingHeads extends App {

  case class Lines(lines: Array[String])

  case class ConversationPartner(actorRef: ActorRef)

  case object Start

  class Interlocutor extends Actor with ActorLogging {
    override def receive: Receive = {
      case ConversationPartner(actorRef) =>
        context.become(gotConversationPartner(actorRef))
    }

    def gotConversationPartner(partner: ActorRef): Receive = {
      case Lines(lines) => context.become(active(lines, partner))
    }

    def active(lines: Array[String], partner: ActorRef): Receive = {
      def talk(): Unit = {
        partner ! lines.head
        if (lines.tail.isEmpty)
          context.become(done)
        else
          context.become(active(lines.tail, partner))
      }
      {
        case Start =>
          talk()
        case line: String =>
          log.info(line)
          talk()


      }
    }

    def done: Receive = {
      case line: String => log.info("I am done, but: .... " + line)
      case _ => log.warning("really I am done")
    }
  }

  val system = ActorSystem("talking-heads")

  val adam = system.actorOf(Props[Interlocutor], "adam")
  val beatrix = system.actorOf(Props[Interlocutor], "btrx")

  adam ! ConversationPartner(beatrix)
  beatrix ! ConversationPartner(adam)

  adam ! Lines(Text.adam)
  beatrix ! Lines(Text.beatrix)

  adam ! Start
  beatrix ! Start

  val terminate: Runnable = () => system.terminate()
  system.scheduler.scheduleOnce(5 seconds, terminate)
}
