name := "RTB"

version := "0.1"

scalaVersion := "2.13.1"

val akkaVersion = "2.6.10"
val akkaHttpVersion = "10.2.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "net.liftweb" %% "lift-json" % "3.4.1"
)

/*
com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "net.liftweb" %% "lift-json" % "3.4.1",
  "com.typesafe.akka" %% "akka-http" % "10.0.15" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % "2.5.8" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor"  % "2.5.8" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.15" % akkaVersion
*/
