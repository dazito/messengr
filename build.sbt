name := """messengr"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.typesafe.akka" %% "akka-persistence" % "2.4.9",
  "com.github.dnvriend" %% "akka-persistence-inmemory" % "1.3.7",
  "com.github.nscala-time" %% "nscala-time" % "2.12.0"
)


resolvers += Resolver.jcenterRepo
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
