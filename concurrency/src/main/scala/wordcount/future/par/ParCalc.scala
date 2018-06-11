package wordcount.future.par

import com.typesafe.scalalogging.LazyLogging
import wordcount.Text
import wordcount.alg.Alg

object ParCalc extends App with Alg with LazyLogging {

  val start = System.nanoTime()

  val wordCount = mapReduce(Text.aTaleOfTwoCities.par)
  val max: (String, Int) = max(wordCount)

  val end = System.nanoTime()

  wordCount.toSeq.foreach(tup => logger.debug(tup.toString))
  logger.info("---> and the winner is: " + max + " <--- calc time is :" +  (end - start)/1000000.0)


}
