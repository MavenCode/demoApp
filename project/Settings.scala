import sbt.Keys._
import sbt._

import scala.language.postfixOps


object Settings extends Build {

    lazy val buildSettings = Seq(
        name := "Bundle Price App",
        normalizedName := "bundlePriceApp",
        organization := "com.mavencode.bundleprice",
        scalaVersion := "2.11.7"
    )

    override lazy val settings = super.settings ++ buildSettings

      val parentSettings = buildSettings ++ Seq(
        publishArtifact := false,
        mainClass in (Compile, run) := Some("com.mavencode.bundleprice.Boot"),
        publish := {}
      )


  lazy val defaultSettings = testSettings ++ Seq(
    autoCompilerPlugins := true,
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    scalacOptions ++= Seq("-encoding", "UTF-8", s"-target:jvm-1.7", "-feature", "-language:_", "-deprecation", "-unchecked", "-Xlint"),
    javacOptions in Compile ++= Seq("-encoding", "UTF-8", "-source", "1.7", "-target", "1.7", "-Xlint:deprecation", "-Xlint:unchecked"),
    run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)),
    ivyLoggingLevel in ThisBuild := UpdateLogging.Quiet,
    unmanagedBase := baseDirectory.value / "custom_lib",
    parallelExecution in ThisBuild := false,
    parallelExecution in Global := false
  )


  val tests = inConfig(Test)(Defaults.testTasks) ++ inConfig(IntegrationTest)(Defaults.itSettings)

  val testOptionSettings = Seq(
    Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
  )

  lazy val testSettings = tests ++ Seq(
    parallelExecution in Test := false,
    parallelExecution in IntegrationTest := false,
    testOptions in Test ++= testOptionSettings,
    testOptions in IntegrationTest ++= testOptionSettings,
    fork in Test := true,
    fork in IntegrationTest := true,
    (compile in IntegrationTest) <<= (compile in Test, compile in IntegrationTest) map { (_, c) => c },
    managedClasspath in IntegrationTest <<= Classpaths.concat(managedClasspath in IntegrationTest, exportedProducts in Test)
  )

}
