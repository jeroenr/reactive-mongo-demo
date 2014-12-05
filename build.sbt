name := """tvi-back-office"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
//  jdbc,
//  anorm,
//  cache,
//  ws
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.3",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.3",
  "com.github.fge" % "json-schema-validator" % "2.1.8",
  "joda-time" % "joda-time" % "2.5",
  "com.github.athieriot" %% "specs2-embedmongo" % "0.7.0" % "test"
)
