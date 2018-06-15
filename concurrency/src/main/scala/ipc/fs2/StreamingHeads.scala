package ipc.fs2


import cats.effect.IO
import cats.instances.all._
import com.typesafe.scalalogging.LazyLogging
import fs2.{Chunk, Pure, Stream}
import ipc.Text

object StreamingHeads extends App with LazyLogging {


  val adam: Stream[Pure, String] = Stream.emits(Text.adam)
  val beatrix: Stream[Pure, String] = Stream.emits(Text.beatrix)

  val interlocutors: Stream[Pure, (String, String)] = adam.zipWith(beatrix){case (l1, l2) => (s"Beatrix said: $l2", s"Adam said: $l1")}
  val log = for {
    i <- interlocutors
    p <- Stream.eval(IO{
      val (a,b) = i
      logger.info(s"$a &&& $b")})
  } yield p

  val drain: IO[Unit] = log.compile.drain
  drain.unsafeRunSync()
}
