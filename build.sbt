name := "collections"
organization := "ohnosequences"
description := "Scala collections"
bucketSuffix := "era7.com"

scalaVersion := "2.12.4"
addCompilerPlugin("ohnosequences" %% "contexts" % "0.5.0")

libraryDependencies ++= Seq(
  "ohnosequences" %% "stuff"   % "0.4.0-95-g636ded7",
  "it.unimi.dsi"  % "fastutil" % "8.1.1"
) ++ testDependencies

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)

dependencyOverrides += "org.scala-lang" % "scala-library" % "2.12.4"

scalacOptions ++= Seq(
  "-Xsource:2.13",
  "-Xlint",
  "-Xfatal-warnings",
  "-Xlog-reflective-calls",
  "-Ywarn-unused",
  "-Ywarn-adapted-args",
  "-opt-warnings:_",
  "-unchecked",
  "-Xstrict-inference",
  "-Yno-predef",
  "-Yno-imports",
  "-Ywarn-unused-import",
  "-Yno-adapted-args",
  "-Ydelambdafy:method",
  "-opt:l:inline",
  "-opt-inline-from:<sources>",
  "-opt:l:method"
  // "-Xfuture",
  // "-Xlog-free-types",
  // "-Xlog-free-terms",
  // "-Ydebug",
  // "-explaintypes",
  // "-uniqid",
  // "-Yopt-log-inline", "_", // noisy
)

// scaladoc
scalacOptions in (Compile, doc) ++= Seq("-groups")
autoAPIMappings := true

// scalafmt
scalafmtVersion := "1.3.0"
scalafmtOnCompile := true

wartremoverErrors in (Compile, compile) := Warts.allBut(Wart.AsInstanceOf,
                                                        Wart.ArrayEquals)
wartremoverWarnings in (Compile, compile) := Warts.allBut(Wart.AsInstanceOf,
                                                          Wart.ArrayEquals)

wartremoverExcluded ++= Seq(
  baseDirectory.value / "src" / "main" / "scala" / "arrays.scala"
)
// shows time for each test:
testOptions in Test += Tests.Argument("-oD")
// disables parallel execs
parallelExecution in Test := false
