lazy val configVersion = "1.3.1"
lazy val junitVersion = "4.12"
lazy val logbackVersion = "1.2.1"
lazy val scalatestVersion = "3.0.1"
lazy val slf4jVersion = "1.7.24"


lazy val buildSettings = Seq(
  organization := "$organization$",
  scalaVersion := "$scala_version$",
  crossScalaVersions := Seq("$scala_version$")
)

lazy val consoleSettings = Seq(
  shellPrompt := { state =>
    val user = System.getProperty("user.name")
    user + "@" + Project.extract(state).currentRef.project + ":sbt> "
  },
  initialCommands in console :=
    """
      |import java.time.Instant
      |import java.util.UUID
    """.stripMargin
)

lazy val dependencySettings = Seq(
  libraryDependencies ++= {
    Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "ch.qos.logback" % "logback-core" % logbackVersion,
      "com.typesafe" % "config" % configVersion,
      "junit" % "junit" % junitVersion % "test",
      "org.scalatest" %% "scalatest" % scalatestVersion % "test",
      "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
      "org.slf4j" % "slf4j-api" % slf4jVersion)
  },
  resolvers ++= Seq(
    Resolver.mavenLocal,
    Resolver.sonatypeRepo("releases"),
    "hohonuuli-bintray" at "http://dl.bintray.com/hohonuuli/maven")
)

lazy val optionSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8", // yes, this is 2 args
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"),
  javacOptions ++= Seq("-target", "1.8", "-source", "1.8"),
  incOptions := incOptions.value.withNameHashing(true),
  updateOptions := updateOptions.value.withCachedResolution(true)
)

// --- Aliases
addCommandAlias("cleanall", ";clean;clean-files")

// --- Modules
lazy val appSettings = buildSettings ++ consoleSettings ++ dependencySettings ++
    optionSettings ++ reformatOnCompileSettings

lazy val apps = Seq("main")  // for sbt-pack

lazy val `$name$` = (project in file("."))
  .settings(appSettings)
  .settings(
    name := "$name$",
    version := "$version$",
    todosTags := Set("TODO", "FIXME", "WTF"),
    fork := true,
    libraryDependencies ++= {
      Seq(
        "com.google.code.gson" % "gson" % "2.8.0"
      )
    }
  )
  .settings( // config sbt-pack
      packAutoSettings ++ Seq(
        packExtraClasspath := apps.map(_ -> Seq("\${PROG_HOME}/conf")).toMap,
        packJvmOpts := apps.map(_ -> Seq("-Duser.timezone=UTC", "-Xmx4g")).toMap,
        packDuplicateJarStrategy := "latest",
        packJarNameConvention := "original"
      )
    )