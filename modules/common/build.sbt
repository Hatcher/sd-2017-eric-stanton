name := "MyApplication-common"

version := "BUILD"

// MANAGED DEPENDENCIES
// Each library below is automatically downloaded from one of the resolvers defined in sbt
// See http://www.scala-sbt.org/0.13.2/docs/Getting-Started/Library-Dependencies.html#the-librarydependencies-key
// Libraries in this project are available to all projects
libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  // Add additional dependencies here; uses Apache Ivy info.

  "com.google.code.gson" % "gson" % "2.2",
  "com.typesafe.akka" % "akka-remote_2.10" % "2.2.3",
  "com.googlecode.json-simple" % "json-simple" % "1.1"
)