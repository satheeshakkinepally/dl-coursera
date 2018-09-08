name in ThisBuild := "dl-coursera"
lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
lazy val commonSettings = Seq(
  organization := "com.akkines.dl",
  version := "0.1"
)
lazy val `dl-commons` = (project in file("dl-commons"))
  .settings(
    libraryDependencies ++=Seq(
      scalaTest
      )
  )
lazy val `dl-coursera` = (project in file("."))
  .settings(commonSettings)
  .aggregate(`dl-commons`,`dl-neuralnets`)
lazy val `dl-neuralnets` = (project in file("dl-neuralnets"))
.settings(
    commonSettings,
    scalaVersion := "2.12.4",
      libraryDependencies ++=Seq(
      scalaTest,
      "org.yaml" % "snakeyaml" % "1.21",
      "com.google.code.gson" % "gson" % "2.8.0",
      "org.slf4j" % "slf4j-api" % "1.7.25"
      )
  ).dependsOn(`dl-commons`)

