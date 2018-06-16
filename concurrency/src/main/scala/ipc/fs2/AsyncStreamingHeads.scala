package ipc.fs2

import cats.effect.{Effect, IO}
import com.typesafe.scalalogging.LazyLogging
import fs2.StreamApp.ExitCode
import fs2.async.mutable.Queue
import fs2.{Stream, StreamApp, async}
import ipc.Text

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.higherKinds

object AsyncStreamingHeads extends StreamApp[IO] {


  case class ConversationPartner(msgQueue: Queue[IO, String], name: String, text: Array[String])
  class Converation(adam: ConversationPartner, beatrix: ConversationPartner)(implicit IO: Effect[IO]) extends LazyLogging {

    def start: Stream[IO, Unit] =
      Stream(
        Stream.emits(adam.text).covary[IO].to(adam.msgQueue.enqueue) concurrently
          Stream.emits(beatrix.text).covary[IO].to(beatrix.msgQueue.enqueue),
        //.map won't work here as you're trying to map a pure value with a side effect. Use `evalMap` instead.
        adam.msgQueue.dequeue.evalMap(line => IO.delay(logger.info(s"${adam.name} received [$line] from ${beatrix.name}"))),// concurrently
        beatrix.msgQueue.dequeue.evalMap(line => IO.delay(logger.info(s"${beatrix.name} received [$line] from ${adam.name}")))

      ).join(3)

  }


  override def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, ExitCode] = {
    def createConversationPartner(name: String, text: Array[String]): Stream[IO, ConversationPartner] = for {
      msgQueue <- Stream.eval(async.boundedQueue[IO, String](1))
      conversationPartner <- Stream.eval(IO.pure(ConversationPartner(msgQueue, name, text)))
    } yield conversationPartner

    Stream.every[IO](500.millis).flatMap { implicit S =>
      for {
        beatrix <- createConversationPartner("btrx", Text.beatrix)
        adam <- createConversationPartner("adam", Text.adam)
        converation = new Converation( adam, beatrix)
        _ <- Stream.sleep[IO](3.seconds) concurrently converation.start.drain
        ec <- Stream.emit(ExitCode.Success).covary[IO]
      } yield ec
    }
  }

}