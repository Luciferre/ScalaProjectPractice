organization := "edu.cmu"

version := "0.0.alpha1"

scalaVersion := "2.11.6"

name := "ScalaPractices"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.9"

libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.3.9"

libraryDependencies += "\"com.typesafe.akka\" %% \"akka-remote\" % \"2.4-SNAPSHOT\""