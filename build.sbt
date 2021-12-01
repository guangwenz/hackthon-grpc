val Http4sVersion = "0.23.6"
val CirceVersion = "0.14.1"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.2.6"
val MunitCatsEffectVersion = "1.0.6"
val gatlingVersion = "3.7.2"

ThisBuild / organization := "com.crunchbase"
ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.6"

val commonSettings = Seq(
  addCompilerPlugin(
    "org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full
  ),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  testFrameworks += new TestFramework("munit.Framework")
)

lazy val server = project
  .in(file("modules/server"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.scalameta" %% "munit" % MunitVersion % Test,
      "org.typelevel" %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "ch.qos.logback" % "logback-classic" % LogbackVersion
    )
  )
  .dependsOn(protobuf)

lazy val protobuf = project
  .in(file("modules/protobuf"))
  .settings(
    libraryDependencies += "io.grpc" % "grpc-netty-shaded" % scalapb.compiler.Version.grpcJavaVersion
  )
  .enablePlugins(Fs2Grpc)

lazy val benchmark = project.in(file("modules/benchmark")).settings(
  commonSettings,
  libraryDependencies ++= Seq(
    "org.http4s" %% "http4s-ember-server" % Http4sVersion,
    "org.http4s" %% "http4s-ember-client" % Http4sVersion,
    "org.http4s" %% "http4s-circe" % Http4sVersion,
    "org.http4s" %% "http4s-dsl" % Http4sVersion,
    "io.circe" %% "circe-generic" % CirceVersion,
    "org.scalameta" %% "munit" % MunitVersion % Test,
    "org.typelevel" %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
    "ch.qos.logback" % "logback-classic" % LogbackVersion,
    "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "test,it",
    "io.gatling" % "gatling-test-framework" % gatlingVersion % "test,it"
  ),
  javaOptions += "--add-opens java.base/jdk.internal.misc=ALL-UNNAMED -Dio.netty.tryReflectionSetAccessible=true"
).enablePlugins(GatlingPlugin).dependsOn(protobuf)


lazy val root = (project in file(".")).aggregate(
  protobuf,
  server,
  benchmark
)
