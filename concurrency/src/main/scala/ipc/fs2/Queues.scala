package ipc.fs2

import cats.effect.{Effect, IO}
import cats.effect.ConcurrentEffect._
import cats.effect.implicits._
import com.typesafe.scalalogging.LazyLogging
import fs2.StreamApp.ExitCode
import fs2.async.mutable.Queue
import fs2.{Stream, StreamApp, async}
import ipc.Text

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.higherKinds

case class ConversationPartner(msgQueue: Queue[IO, String], name: String)
class Interlocutor(/*q1: Queue[IO, String],*/name: String, conversationPartner: ConversationPartner)(implicit IO: Effect[IO]) extends LazyLogging {

  def start(text: Array[String]): Stream[IO, Unit] =
    Stream(
//      Stream.emits(text).covary[IO].to(q1.enqueue),
//      q1.dequeue.to(q2.enqueue),
      Stream.emits(text).covary[IO].to(conversationPartner.msgQueue.enqueue),
//      q1.dequeue.to(q2.enqueue),
      //.map won't work here as you're trying to map a pure value with a side effect. Use `evalMap` instead.
      conversationPartner.msgQueue.dequeue.evalMap(line => IO.delay(logger.info(s"${conversationPartner.name} received [$line] from $name")))
    ).join(3)

}



object Fifo extends StreamApp[IO] {

  override def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, ExitCode] = {
    def createConversationPartner(name: String): Stream[IO, ConversationPartner] = for {
      msgQueue <- Stream.eval(async.boundedQueue[IO, String](1))
      conversationPartner <- Stream.eval(IO.pure(ConversationPartner(msgQueue, name)))
    } yield conversationPartner

    Stream.every[IO](500.millis).flatMap { implicit S =>
      for {
        beatrix <- createConversationPartner("btrx")
        adam <- createConversationPartner("adam")
        adamInterlocutor = new Interlocutor("adam", beatrix)
        btrxInterlocutor = new Interlocutor("btrx", adam)
        _ <- Stream.sleep[IO](2.seconds)
        _ <- Stream.sleep[IO](3.seconds) concurrently
          (adamInterlocutor.start(Text.adam).drain concurrently
          btrxInterlocutor.start(Text.beatrix).drain)
        ec <- Stream.emit(ExitCode.Success).covary[IO]
      } yield ec
    }
  }

}


//class Buffering[F[_]](q1: Queue[F, Int], q2: Queue[F, Int])(implicit F: Effect[F]) {
//
//  def start: Stream[F, Unit] =
//    Stream(
//      Stream.range(0, 1000).covary[F].to(q1.enqueue),
//      q1.dequeue.to(q2.enqueue),
//      //.map won't work here as you're trying to map a pure value with a side effect. Use `evalMap` instead.
//      q2.dequeue.evalMap(n => F.delay(println(s"Pulling out $n from Queue #2")))
//    ).join(3)
//
//}
//
//
//
//class Fifo[F[_]: Effect] extends StreamApp[F] {
//
//  override def stream(args: List[String], requestShutdown: F[Unit]): fs2.Stream[F, ExitCode] =
//    Stream.awakeEvery[F](500.millis).flatMap { implicit S =>
//      for {
//        q1 <- Stream.eval(async.boundedQueue[F, Int](1))
//        q2 <- Stream.eval(async.boundedQueue[F, Int](100))
//        bp = new Buffering[F](q1, q2)
//        _ <- Stream.sleep[F](5.seconds)
//        ec <- Stream.emit(ExitCode.Success).covary[F] concurrently bp.start.drain
//      } yield ec
//    }
//
//}
