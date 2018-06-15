package ipc

object Text {
  
  private val questions = """
                               |I can't seem to face up to the facts
                               |I'm tense and nervous and I can't relax
                               |I can't sleep 'cause my bed's on fire
                               |Don't touch me I'm a real live wire
                               |Psycho Killer
                               |Qu'est-ce que c'est
                               |Fa-fa-fa-fa-fa-fa-fa-fa-fa-far better
                               |Run run run run run run run away oh oh
                               |Psycho Killer
                               |Qu'est-ce que c'est
                               |Fa-fa-fa-fa-fa-fa-fa-fa-fa-far better
                               |Run, run, run, run, run, run, run, away oh oh oh
                               |Yeah yeah yeah yeah!
                               |You start a conversation you can't even finish it
                               |You're talking a lot, but you're not saying anything
                               |When I have nothing to say, my lips are sealed
                               |Say something once, why say it again?
                               |Psycho Killer
                               |Qu'est-ce que c'est
                               |Fa-fa-fa-fa-fa-fa-fa-fa-fa-far better
                               |Run run run run run run run away oh oh oh
                               |Psycho Killer
                               |Qu'est-ce que c'est
                               |Fa-fa-fa-fa-fa-fa-fa-fa-fa-far better
                               |Run, run, run, run, run, run, run, away oh oh oh oh!
                               |Yeah yeah yeah yeah!
                               |Ce que j'ai fais, ce soir la
                               |Ce qu'elle a dit, ce soir la
                               |Realisant mon espoir
                               |Je me lance, vers la gloire, OK
                               |Yeah yeah yeah yeah yeah yeah yeah yeah yeah yeah
                               |We are vain and we are blind
                               |I hate people when they're not polite
                               |Psycho Killer
                               |Qu'est-ce que c'est
                               |Fa-fa-fa-fa-fa-fa-fa-fa-fa-far better
                               |Run run run run run run run away oh oh oh
                               |Psycho Killer
                               |Qu'est-ce que c'est
                               |Fa-fa-fa-fa-fa-fa-fa-fa-fa-far better
                               |Run, run, run, run, run, run, run, away oh oh oh
                               |Yeah yeah yeah yeah oh!
 """.stripMargin

  private val questionsarr = questions.split("\n").filterNot(_.isEmpty)

  private val split = questionsarr.zipWithIndex

  val adam: Array[String] = split.filter(_._2 % 2 == 0).map(_._1)
  val beatrix: Array[String] = split.filter(_._2 % 2 != 0).map(_._1)

}
