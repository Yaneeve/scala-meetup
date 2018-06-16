package ipc.fs2

import cats.effect.{Effect, IO}
import cats.effect.ConcurrentEffect._
import cats.effect.implicits._
import fs2.StreamApp.ExitCode
import fs2.async.mutable.Queue
import fs2.{Stream, StreamApp, async}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.higherKinds

class Buffering(q1: Queue[IO, Int], q2: Queue[IO, Int])(implicit IO: Effect[IO]) {

  def start: Stream[IO, Unit] =
    Stream(
      Stream.range(0, 1000).covary[IO].to(q1.enqueue),
      q1.dequeue.to(q2.enqueue),
      //.map won't work here as you're trying to map a pure value with a side effect. Use `evalMap` instead.
      q2.dequeue.evalMap(n => IO.delay(println(s"Pulling out $n from Queue #2")))
    ).join(3)

}



object Fifo extends StreamApp[IO] {

  override def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, ExitCode] =
    Stream.every[IO](500.millis).flatMap { implicit S =>
      for {
        q1 <- Stream.eval(async.boundedQueue[IO, Int](1))
        q2 <- Stream.eval(async.boundedQueue[IO, Int](100))
        bp = new Buffering(q1, q2)
        _ <- Stream.sleep[IO](3.seconds) concurrently bp.start.drain
        ec <- Stream.emit(ExitCode.Success).covary[IO]
      } yield ec
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
