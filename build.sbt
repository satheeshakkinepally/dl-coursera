name in ThisBuild := "dl-coursera"
logLevel := Level.Debug
//resolvers += "ImageJ Public" at "http://maven.imagej.net/content/repositories/public/"
//resolvers += Resolver.url("ImageJ Public", url("http://maven.imagej.net/content/repositories/public/"))
lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
lazy val nd4jVersion = "1.0.0-alpha"
//lazy val nd4jVersion = "0.9.1"
lazy val commonSettings = Seq(
  organization := "com.akkines.dl",
  scalaVersion := "2.11.12",
  version := "0.1"
)

lazy val `dl-commons` = (project in file("dl-commons"))
  .settings(
    commonSettings,
    libraryDependencies ++=Seq(
      scalaTest,
      "org.nd4j" % "nd4j-native-platform" % nd4jVersion,
      "org.nd4j" % "nd4j-api" % nd4jVersion,
      "org.nd4j" %% "nd4s" % nd4jVersion,
      "commons-io" % "commons-io" % "2.6",
      "org.apache.commons" % "commons-lang3" % "3.8"
    )
  )
lazy val `dl-coursera` = (project in file("."))
  .settings(commonSettings)
  .dependsOn(`dl-commons`,`dl-neuralnets`)
  .aggregate(`dl-commons`,`dl-neuralnets`)

lazy val `dl-neuralnets` = (project in file("dl-neuralnets"))
.settings(
    commonSettings,
      libraryDependencies ++=Seq(
      scalaTest,
      "org.yaml" % "snakeyaml" % "1.21",
      "com.google.code.gson" % "gson" % "2.8.0",
      "org.slf4j" % "slf4j-api" % "1.7.25"
      )
  ).dependsOn(`dl-commons`)

