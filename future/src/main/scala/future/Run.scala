package future

import java.util.concurrent.Executors
import java.util.concurrent.atomic.{AtomicReference, AtomicStampedReference}

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.concurrent.Await.result
import scala.concurrent.duration._
object Run extends App with LazyLogging {


  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(200))

  val t: Seq[Future[Unit]] = Transcript.adam.map(Future(_)).map(_.map(phrase => logger.info(phrase)))

  logger.info("begin 1")
  result(Future.sequence(t), 5 seconds)

  logger.info("===================================")
  val t2 = Transcript.adam.foldLeft(Future(logger.info("begin 2"))){
    case (f, phrase) =>
      val eventualUnit = Future(logger.info(phrase))
      f.flatMap(_ => eventualUnit)

  }

  result(t2, 5 seconds)


  logger.info("===================================")
  val t3 = Transcript.adam.foldLeft(Future(logger.info("begin 3"))){
    case (f, phrase) =>
      lazy val eventualUnit = Future.successful(logger.info(phrase))
      f.flatMap(_ => eventualUnit)

  }

  result(t3, 5 seconds)

  logger.info("===================================")
  val t4 = Transcript.adam.foldLeft(Future(logger.info("begin 4"))){
    case (f, phrase) =>
      lazy val eventualUnit = logger.info(phrase)
      f.map(_ => eventualUnit)

  }

  result(t4, 5 seconds)

  logger.info("===================================")

  val t5 = Future{
    logger.info("begin 5")
    Transcript.adam.foreach(logger.info(_))
  }

  result(t5, 5 seconds)

  logger.info("===================================")

  val interlocutor1 = new AtomicReference[String]
  val interlocutor2 = new AtomicReference[String]

  Future{
    logger.info("interlocutor 1 - begin ")
    Transcript.adam.foreach{phrase => interlocutor2.set(phrase); logger.info("1 says: " + interlocutor1.get())}
  }

  Future{
    logger.info("interlocutor 2 - begin ")
    Transcript.beatrix.foreach{phrase => interlocutor1.set(phrase); logger.info("2 says: " + interlocutor2.get())}
  }
  import java.util.concurrent.ArrayBlockingQueue

  val int1Queue: ArrayBlockingQueue[String] = new ArrayBlockingQueue[String](Transcript.beatrix.size)
  val int2Queue: ArrayBlockingQueue[String] = new ArrayBlockingQueue[String](Transcript.adam.size)
  logger.info("===================================")
  Future{
    logger.info("interlocutor [queue] 1 - begin ")
    Transcript.adam.foreach{phrase => int1Queue.add(phrase); logger.info("1 [queue] says: " + int2Queue.poll())}
  }

  Future{
    logger.info("interlocutor [queue] 2 - begin ")
    Transcript.beatrix.foreach{phrase => int2Queue.add(phrase); logger.info("2 [queue] says: " + int1Queue.poll())}
  }
}
