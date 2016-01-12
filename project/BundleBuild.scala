import sbt._
import sbt.Keys._

object BundleBuild extends Build {

  import Settings._

  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = parentSettings,
    dependencies = Seq(market),
    aggregate = Seq(market, akkaHttpSwagger)
  )

  lazy val market = Project(
    id = "market",
    base = file("./market"),
    dependencies = Seq(akkaHttpSwagger),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.market)
  )

  lazy val akkaHttpSwagger = Project(
    id = "akka-http-swagger",
    base = file("./akka-http-swagger"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.akkaHttpSwagger)
  )

}

object Dependencies {

  object Compile {

    val Akka           		= "2.3.9"
    val Logback	   		 		= "1.1.3"
    val AkkaE	   			 		= "2.0-M1"
    val Slf4j	   					= "1.7.12"

    val akkaActor         = "com.typesafe.akka"                 %% "akka-actor"                                 % Akka
    val akkaPersistence   = "com.typesafe.akka"                 %% "akka-persistence-experimental"              % Akka
    val akkaSlf4j         = "com.typesafe.akka"                 %% "akka-slf4j"                                 % Akka
    val akkaStreams       = "com.typesafe.akka"                 %%  "akka-stream-experimental"                  % AkkaE
    val akkaHTTPCore      = "com.typesafe.akka"                 %%  "akka-http-core-experimental"               % AkkaE
    val akkaHTTP          = "com.typesafe.akka"                 %%  "akka-http-experimental"                    % AkkaE
    val akkaHTTPxml       = "com.typesafe.akka"                 %% "akka-http-xml-experimental"                 % AkkaE
    val akkaHTTPSpray     = "com.typesafe.akka"                 %% "akka-http-spray-json-experimental"          % AkkaE
    val akkaHTTPTest      = "com.typesafe.akka"                 %% "akka-http-testkit-experimental"             % AkkaE

    val scalaTest         = "org.scalatest"                     %%  "scalatest"                                 % "2.2.6" % "test"
    val akkaText          = "com.typesafe.akka"                 %%  "akka-testkit"                              % Akka    % "test"

    val swaggerCore      = "com.wordnik"                       %% "swagger-core"                               % "1.3.12"
    val javaWsApi         = "javax.ws.rs"                       % "jsr311-api"                                  % "1.1.1"
    val nScalaTime        = "com.github.nscala-time"            %% "nscala-time"                                % "2.0.0" % "test"

    val akkaHttpPlayJson  = "de.heikoseeberger"                 %% "akka-http-play-json"                        % "1.1.0"
    val jacksonCore       = "com.fasterxml.jackson.core"        %  "jackson-core"                               %  "2.6.1"
    val jacksonAnno       = "com.fasterxml.jackson.core"        %  "jackson-annotations"                        %  "2.6.1"
    val jacksonDBind      = "com.fasterxml.jackson.core"        %  "jackson-databind"                           %  "2.6.1"

    val logback           = "ch.qos.logback"                    % "logback-classic"                             % Logback
    val slf4jApi          = "org.slf4j"                         % "slf4j-api"                                   % Slf4j
    val scalaLogging      = "com.typesafe.scala-logging"        %% "scala-logging"                              % "3.1.0"

    val json4s            = "org.json4s"                        %% "json4s-native"                              % "3.2.11"
    val json4sJackson     = "org.json4s"                        %% "json4s-jackson"                             % "3.2.11" % "test"

  }

  import Compile._

  val logging = Seq(logback, slf4jApi)
  val market = logging ++ Seq(akkaActor, akkaPersistence, akkaStreams, akkaHTTPCore, akkaHTTP, akkaHTTPSpray, akkaHTTPTest, akkaText, scalaTest)
  val akkaHttpSwagger = Seq(akkaActor, akkaStreams, akkaHTTPTest, akkaHTTP, akkaHTTPCore, akkaHTTPSpray, akkaHTTPxml, swaggerCore, javaWsApi, scalaTest, nScalaTime, scalaLogging, json4s, json4sJackson)

}


