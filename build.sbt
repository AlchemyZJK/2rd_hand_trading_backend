ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val root = (project in file("."))
  .settings(
    name := "2rd_hand_trading_backend",
    libraryDependencies ++= Seq (
      "mysql" % "mysql-connector-java" % "8.0.27",
      "org.apache.commons" % "commons-dbcp2" % "2.8.0",
      "org.typelevel" %% "cats-effect" % "3.3.5" withSources() withJavadoc()
    )
  )
