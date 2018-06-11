name := "scala-meetup"

lazy val commonSettings = Seq(
  organization := "yaneeve",
  version := "0.1.0",
  scalaVersion := "2.12.6",
  scalacOptions += "-Ypartial-unification"
)

lazy val commonLibs = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"
)


lazy val future = project.settings(commonSettings,
  libraryDependencies := commonLibs ++ Seq(
    "org.typelevel" %% "cats-core" % "1.0.1",
    "org.typelevel" %% "cats-kernel" % "1.0.1",
    "org.typelevel" %% "cats-macros" % "1.0.1"
  ))

lazy val ioeffect = project.settings(commonSettings,
  libraryDependencies := commonLibs ++ Seq(
    "org.typelevel" %% "cats-core" % "1.0.1",
    "org.typelevel" %% "cats-kernel" % "1.0.1",
    "org.typelevel" %% "cats-macros" % "1.0.1",
    "org.typelevel" %% "cats-effect" % "1.0.0-RC2"
  ))