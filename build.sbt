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

