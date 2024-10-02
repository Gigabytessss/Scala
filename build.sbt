ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "Temp"
  )

libraryDependencies += "com.knuddels" % "jtokkit" % "1.1.0"