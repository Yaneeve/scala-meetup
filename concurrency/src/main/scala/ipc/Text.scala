package ipc

object Text {
  
  private val questions = raw"""
 What was the last funny video you saw?
                       
 What do you do to get rid of stress?
                       
 What is something you are obsessed with?
                       
 Who is your favorite entertainer (comedian, musician, actor, etc.)?
                       
 What’s your favorite way to waste time?
                       
 Do you have any pets? What are their names?
                       
 Where did you go last weekend? What did you do?
                       
 What are you going to do this weekend?
                       
 What is something that is popular now that annoys you?
                       
 What did you do on your last vacation?

 What was the last time you worked incredibly hard?
                       
 Are you very active of do you prefer to just relax in your free time?
                       
 What do you do when you hang out with your friends?
                       
 Who is your oldest friend? Where did you meet them?
                       
 What’s the best / worst thing about your work / school?
                       
 If you had intro music, what song would it be? Why?
                       
 What were you really into when you were a kid?
                       
 If you could have any animal as a pet, what animal would you choose?
                       
 What three words best describe you?
                       
 What would be your perfect weekend?
                       
 What do you think of tattoos? Do you have any?
                       
 What’s your favorite number? Why?
                       
 What’s the most useful thing you own?
                       
 Have you ever saved an animal’s life? How about a person’s life?
                       
 If you opened a business, what kind of business would it be?
                       
 Are you a very organized person?
                       
 Have you ever spoke in front of a large group of people? How did it go?
                       
 What is the strangest dream you have ever had?
                       
 What is a controversial opinion you have?
                       
 Who in your life brings you the most joy?
                       
 Who had the biggest impact on the person you have become?
                       
 What is the most annoying habit someone can have?
                       
 Where is the most beautiful place you have been?
                       
 Where do you spend most of your free time / day?
                       
 Who was your best friend in elementary school?
                       
 How often do you stay up past 3 a.m.?
                       
 What’s your favorite season? Why?
                       
 Which is more important, a great car or a great house? Why?
                       
 What animal or insect do you wish humans could eradicate?
                       
 Where is the most beautiful place near where you live?
                       
 What do you bring with you everywhere you go?
                       
 How much time do you spend on the internet? What do you usually do?
                       
 What is the most disgusting habit some people have?
                       
 Where and when was the most amazing sunset you have ever seen?
                       
 Which recent news story is the most interesting?
                       
 Where is the worst place you have been stuck for a long time?
                       
 If you had to change your name, what would your new name be?
                       
 What is something that really annoys you but doesn’t bother most people?
                       
 What word or saying from the past do you think should come back?
                       
 How should success be measured? By that measurement, who is the most successful person you know?
                       
 What is your guilty pleasure?
                       
 Was there ever an event in your life that defied explanation?
                       
 If you could learn the answer to one question about your future, what would the question be?
                       
 Has anyone ever saved your life?
                       
 What benefit do you bring to the group when you hang out with friends?
                       
 How often do you curse?
                       
 What trends did you follow when you were younger?
                       
 What do you fear is hiding in the dark?
                       
 What was the best time period of your life? What do you think will be the best time period of your entire life?
                       
 What do you do to improve your mood when you are in a bad mood?
                       
 What is the silliest fear you have?
                       
 What are some things you want to accomplish before you die?
                       
 What is the best room in your house? Why?
                       
 Who is someone who is popular now that you really like? Why do you like them so much?
                       
 Where is the best place to take a date?
                       
 What smell brings back great memories?
                       
 How often do you help others? Who do you help? How do you help?
                       
 What are you best at?
                       
 What makes you nervous?
                       
 Who, besides your parents, had the biggest impact on your life?
                       
 What weird or useless talent do you have?
                       
 What are some strange beliefs that some people have?
                       
 Who would be the worst person to be stuck in an elevator with? How about the best person to be stuck in an elevator with?
                       
 What’s the funniest TV series you have seen?
                       
 Which TV show do you want your life to be like?
                       
 
 If you could bring back one TV show that was cancelled, which one would you bring back?
                       
 How have TV shows changed over the years?
                       
 What’s the best sitcom past or present?
                       
 What’s the best show currently on TV?
                       
 What do you think about game shows? Do you have a favorite one?
                       
 What’s the most underrated or overrated TV show?
                       
 What do you think about reality TV? Why is it so popular?
                       
 Do you like reality TV shows? Why or why not? If so, which ones?
                       
 What will be the future of TV shows?
                       
 What was the best birthday wish or gift you’ve ever received?
                       
 How often do you binge watch shows?
                       
 What cartoons did you watch as a child?"""

  private val questionsarr = questions.split("\n").map(_.trim).filterNot(_.isEmpty)

  private val split = questionsarr.splitAt(questionsarr.length/2)

  val adam: Array[String] = split._1
  val beatrix: Array[String] = split._2

}
