package wordcount.sequential

import com.typesafe.scalalogging.LazyLogging
import wordcount.Text

object Calc extends App with LazyLogging {

  val start = System.nanoTime()

  val mapReduce = Text.aTaleOfTwoCities.groupBy(identity).map { case (key, arr) => (key, arr.length) }
  val max: (String, Int) = mapReduce.maxBy(_._2)

  val end = System.nanoTime()

  mapReduce.toSeq.foreach(tup => logger.debug(tup.toString))
  logger.info("---> and the winner is: " + max + " <--- calc time is :" +  (end - start)/1000000.0)


}
