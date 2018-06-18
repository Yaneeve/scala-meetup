package ipc.akka

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.typesafe.scalalogging.LazyLogging
import ipc.Text

object StreamingHeads extends App with LazyLogging {

  implicit val system = ActorSystem("streaming-heads")
  implicit val materializer = ActorMaterializer()


  val adam: Source[String, NotUsed] = Source.apply[String](Text.adam.to[scala.collection.immutable.Iterable])
  val beatrix: Source[String, NotUsed] = Source.apply[String](Text.beatrix.to[scala.collection.immutable.Iterable])
  val zipped: Source[(String, String), NotUsed] = adam zip beatrix

    zipped.runForeach {case (a, b) =>
        logger.info(s"$a")
        logger.info(s"$b")
      }



}
