package future

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

abstract class Interlocutor(name: String, phrases: Seq[String])(implicit ec: ExecutionContext) extends LazyLogging {

//  phrases.toStream.


  def arrive(): Future[Unit] = Future{()}

  def listen(msg: String): Future[Unit] = Future.successful{
    logger.info(s"$name is listening to: $msg")
  }

  def talk(partner: Interlocutor): Future[Unit] = ??? //partner.listen(phrases.)


}

case object Adam extends Interlocutor("Adam", Transcript.adam)(scala.concurrent.ExecutionContext.global)