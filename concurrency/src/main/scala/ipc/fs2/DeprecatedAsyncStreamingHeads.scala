package ipc.fs2

import cats.effect.{Effect, IO}
import cats.effect.Concurrent
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import fs2.StreamApp.ExitCode
import fs2.async.mutable.Topic
import fs2.{Pure, Sink, Stream, StreamApp, async}
import ipc.Text

import scala.concurrent.ExecutionContext
import scala.language.higherKinds
import scala.concurrent.ExecutionContext.Implicits.global

object DeprecatedAsyncStreamingHeads { //extends StreamApp[IO] with LazyLogging {


  val adam: Stream[Pure, String] = Stream.emits(Text.adam)
  val beatrix: Stream[Pure, String] = Stream.emits(Text.beatrix)

//  val interlocutors: Stream[Pure, (String, String)] = adam.zipWith(beatrix){case (l1, l2) => (s"Beatrix said: $l2", s"Adam said: $l1")}
//
//  val log = for {
//    q <- Stream.eval(async.unboundedQueue[IO,Either[Throwable, String]])
//    _ <- Stream.eval { IO.delay(h.withRows(e => async.unsafeRunAsync(q.enqueue1(e))(_ => IO.unit))) }
//    i <- interlocutors
//    p <- Stream.eval(IO{
//      val (a,b) = i
//      logger.info(s"$a &&& $b")})
//  } yield p
//
//  val drain: IO[Unit] = log.compile.drain
//  drain.unsafeRunSync()






//
//  def sharedTopicStream[F[_]: Effect](topicId: String)(implicit ec: ExecutionContext): Stream[F, Topic[F, String]] =
//    Stream.eval(async.topic[F, String](s"Topic $topicId start"))
//
//  def addPublisher[F[_]](topic: Topic[F, String], value: String): Stream[F, Unit] =
//    Stream.emit(value).covary[F].repeat.to(topic.publish)
//
//  def addSubscriber[F[_]](topic: Topic[F, String]): Stream[F, String] =
//    topic
//      .subscribe(10)
//      .take(4)
//
//  // a request that adds a publisher to the topic
//  def requestAddPublisher[F[_]](value: String, topic: Topic[F, String]): Stream[F, Unit] =
//    addPublisher(topic, value)
//
//  // a request that adds a subscriber to the topic
//  def requestAddSubscriber[F[_]](topic: Topic[F, String])(implicit F: Effect[F]): Stream[F, Unit] =
//    addSubscriber(topic).to(Sink.showLinesStdOut)
//
//  def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
//    import scala.concurrent.ExecutionContext.Implicits.global
//    // we simulate  requests that work on a common topic.
//    val sharedTopic = sharedTopicStream[IO]("sharedTopic")
//
//    // sharedTopic is passed to your Services, which use it as necessary
//    sharedTopic.flatMap { topic ⇒
//      requestAddPublisher("publisher1", topic) concurrently
//        requestAddPublisher("publisher2", topic) concurrently
//        requestAddSubscriber(topic) concurrently
//        requestAddSubscriber(topic)
//
//    }.drain //++ Stream.emit(ExitCode.Success)
//  }
//
////  stream(Text.adam.toList, IO.unit)
}
