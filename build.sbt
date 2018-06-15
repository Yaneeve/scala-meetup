name := "scala-meetup"

lazy val commonSettings = Seq(
  organization := "yaneeve",
  version := "0.1.0",
  scalaVersion := "2.12.6",
  scalacOptions += "-Ypartial-unification",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
)

lazy val commonLibs = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
  "org.typelevel" %% "cats-core" % "1.1.0",
  "org.typelevel" %% "cats-kernel" % "1.1.0",
  "org.typelevel" %% "cats-macros" % "1.1.0"
)

lazy val concurrency = project.settings(commonSettings,
  libraryDependencies := commonLibs ++ Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.5.13",
    "org.typelevel" %% "cats-effect" % "1.0.0-RC2",
    "co.fs2" %% "fs2-core" % "0.10.4" // For cats 1.1.0 and cats-effect 0.10

  )
)

lazy val errors = project.settings(commonSettings,
  libraryDependencies := commonLibs++ Seq(
    "org.typelevel" %% "cats-effect" % "1.0.0-RC2"
  ))