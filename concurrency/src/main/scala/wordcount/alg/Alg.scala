package wordcount.alg

import com.typesafe.scalalogging.LazyLogging

import scala.collection.parallel.ParMap
import scala.collection.parallel.mutable.ParArray

trait Alg {
  self: LazyLogging =>

  def mapReduce(text: Array[String], partition: Option[String] = None): Map[String, Int] = {
    logger.info(s"HAL ${partition.getOrElse("dunno")} here, just to inform you that I will take care of ${text.head} ...")
    text.groupBy(identity).map { case (key, arr) => (key, arr.length) }
  }
  def max(wordCount: Iterable[(String, Int)]): (String, Int) = wordCount.maxBy(_._2)
}

trait ParAlg {
  self: LazyLogging =>

  def mapReduce(text: ParArray[String]): ParMap[String, Int] = {
    logger.info(s"HAL dunno here, just to inform you that I will take care of ${text.head} ...")
    text.groupBy(identity).map { case (key, arr) => (key, arr.length) }
  }

  def max(wordCount: ParMap[String, Int]): (String, Int) = wordCount.maxBy(_._2)

}