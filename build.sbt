import play.ebean.sbt.PlayEbean
import play.sbt.PlayImport._
import play.sbt.PlayJava

libraryDependencies ++= Seq(
  // Select Play modules
  jdbc,      // The JDBC connection pool and the play.api.db API
  javaCore,  // The core Java API

  // WebJars pull in client-side web libraries
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "2.3.1",

  "mysql" % "mysql-connector-java" % "5.1.28",
  "org.apache.directory.studio" % "org.apache.commons.io" % "2.4",
  "com.google.code.gson" % "gson" % "2.2",
  "com.typesafe.akka" % "akka-remote_2.10" % "2.2.3",
  "com.googlecode.json-simple" % "json-simple" % "1.1",
  "org.jsoup" % "jsoup" % "1.9.2"
)

libraryDependencies += evolutions


// SETUP MULTIPLE PROJECTS
// sbt and Play Framework support the concept of sub-projects. Each subproject represents a major module in our project.

// Root project. Container for all the projects
lazy val root = project.in(file("."))
  .enablePlugins(PlayJava, PlayEbean)
  .aggregate(common, passages, words, math)
  .dependsOn(common, passages, words, math)

// Contains all files and libraries shared across other projects
lazy val common = project.in(file("modules/common"))
  .enablePlugins(PlayJava, PlayEbean)

lazy val math = project.in(file("modules/math"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(common)
  
// Contains files specific to CAPITAL Passages
// (Passages project depends on "common")
lazy val passages = project.in(file("modules/passages"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(common)

// Contains files specific to CAPITAL Words
// (Words project depends on "common")
lazy val words = project.in(file("modules/words"))
  .enablePlugins(PlayJava, PlayEbean)
  .dependsOn(common)