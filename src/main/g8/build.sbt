// PROJECT PROPERTIES ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Implements strict static analysis recommended at
// https://docs.google.com/presentation/d/1tCmphnyP3F5WUtd1iNLub0TWRVyfTJsqhvzYhNS41WY/pub?start=false#slide=id.p

organization in ThisBuild := "$organization$"

name := "$name$"

version in ThisBuild := "$version$"

scalaVersion in ThisBuild := "$scala_version$"

//crossScalaVersions := Seq("$scala_version$", "2.10.5", "2.11.6")

// https://tpolecat.github.io/2014/04/11/scalac-flags.html
scalacOptions in ThisBuild ++= Seq(
  "-deprecation",           
  "-encoding", "UTF-8",       // yes, this is 2 args
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
  "-Xfuture")

javacOptions in ThisBuild ++= Seq("-target", "1.8", "-source","1.8")

// DEPENDENCIES ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

updateOptions := updateOptions.value.withCachedResolution(true) 

// Add SLF4J, Logback and testing libs
libraryDependencies ++= {
  val slf4jVersion = "1.7.10"
  val logbackVersion = "1.1.2"
  Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "ch.qos.logback" % "logback-core" % logbackVersion,
    "junit" % "junit" % "4.12" % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
    "org.slf4j" % "slf4j-api" % slf4jVersion)
}

resolvers in ThisBuild ++= Seq(Resolver.mavenLocal,
    "mbari-maven-repository" at "https://mbari-maven-repository.googlecode.com/svn/repository")

//publishMavenStyle := true

//publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

// OTHER SETTINGS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Adds commands for dependency reporting
net.virtualvoid.sbt.graph.Plugin.graphSettings

// set the prompt (for this build) to include the project id.
shellPrompt in ThisBuild := { state => 
  val user = System.getProperty("user.name")
  "\n" + user + "@" + Project.extract(state).currentRef.project + "\nsbt> " 
}

// Add this setting to your project to generate a version report (See ExtendedBuild.scala too.)
// Use as 'sbt versionReport' or 'sbt version-report'
versionReport <<= (externalDependencyClasspath in Compile, streams) map {
  (cp: Seq[Attributed[File]], streams) =>
    val report = cp.map {
      attributed =>
        attributed.get(Keys.moduleID.key) match {
          case Some(moduleId) => "%40s %20s %10s %10s".format(
            moduleId.organization,
            moduleId.name,
            moduleId.revision,
            moduleId.configurations.getOrElse("")
          )
          case None =>
            // unmanaged JAR, just
            attributed.data.getAbsolutePath
        }
    }.sortBy(a => a.trim.split("\\\\s+").map(_.toUpperCase).take(2).last).mkString("\n")
    streams.log.info(report)
    report
}

// Code for adding a version.propertes file
gitHeadCommitSha := scala.util.Try(Process("git rev-parse HEAD").lines.head).getOrElse("")

makeVersionProperties := {
  val propFile = (resourceManaged in Compile).value / "version.properties"
  val content = "version=%s" format (gitHeadCommitSha.value)
  IO.write(propFile, content)
  Seq(propFile)
}

resourceGenerators in Compile <+= makeVersionProperties

// For sbt-pack
packAutoSettings

// Format code on save with scalariform
import scalariform.formatter.preferences._

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(IndentSpaces, 2)
  .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
  .setPreference(DoubleIndentClassDeclaration, true)
  
// Fail if style is bad
scalastyleFailOnError := true

// https://github.com/leifwickland/static-analysis-skeleton
// Configure Lint target
// A configuration which is like 'compile' except it performs additional static analysis.
// Execute static analysis via 'lint:compile'
val LintTarget = config("lint").extend(Compile)

addMainSourcesToLintTarget 

addSlowScalacSwitchesToLintTarget

addWartRemoverToLintTarget

removeWartRemoverFromCompileTarget 

addFoursquareLinterToLintTarget 

removeFoursquareLinterFromCompileTarget 

def addMainSourcesToLintTarget = {
  inConfig(LintTarget) {
    // I posted http://stackoverflow.com/questions/27575140/ and got back the bit below as the magic necessary
    // to create a separate lint target which we can run slow static analysis on.
    Defaults.compileSettings ++ Seq(
      sources in LintTarget := {
        val lintSources = (sources in LintTarget).value
        lintSources ++ (sources in Compile).value
      }
    )
  }
}

def addSlowScalacSwitchesToLintTarget = {
  inConfig(LintTarget) {
    // In addition to everything we normally do when we compile, we can add additional scalac switches which are
    // normally too time consuming to run.
    scalacOptions in LintTarget ++= Seq(
      // As it says on the tin, detects unused imports. This is too slow to always include in the build.
      "-Ywarn-unused-import",
      //This produces errors you don't want in development, but is useful.
      "-Ywarn-dead-code"
    )
  }
}

def addWartRemoverToLintTarget = {
  import wartremover._
  import Wart._
  // I didn't simply include WartRemove in the build all the time because it roughly tripled compile time.
  inConfig(LintTarget) {
    // Ban inferring Any, Serializable, and Product because such inferrence usually indicates a code error.
    wartremoverErrors ++= Seq(
      Wart.Any,
      Wart.Any2StringAdd, // // Ban applying Scala's implicit any2String because it usually indicates a code error.
      Wart.EitherProjectionPartial,
      Wart.OptionPartial,
      Wart.Product,
      Wart.Serializable,
      Wart.ListOps,
      Wart.Nothing)
  }
}

def removeWartRemoverFromCompileTarget = {
  // WartRemover's sbt plugin calls addCompilerPlugin which always adds directly to the Compile configuration.
  // The bit below removes all switches that could be passed to scalac about WartRemover during a non-lint compile.
  scalacOptions in Compile := (scalacOptions in Compile).value filterNot { switch =>
    switch.startsWith("-P:wartremover:") ||
    "^-Xplugin:.*/org[.]brianmckenna/.*wartremover.*[.]jar\$".r.pattern.matcher(switch).find
  }
}

def addFoursquareLinterToLintTarget = {
  Seq(
    resolvers += "Linter Repository" at "https://hairyfotr.github.io/linteRepo/releases",
    addCompilerPlugin("com.foursquare.lint" %% "linter" % "0.1.9"),
    // See https://github.com/HairyFotr/linter#list-of-implemented-checks for a list of checks that foursquare linter
    // implements
    // By default linter enables all checks.
    // I don't mind using match on boolean variables.
    scalacOptions in LintTarget += "-P:linter:disable:PreferIfToBooleanMatch"
  )
}

def removeFoursquareLinterFromCompileTarget = {
  // We call addCompilerPlugin in project/plugins.sbt to add a depenency on the foursquare linter so that sbt magically
  // manages the JAR for us.  Unfortunately, addCompilerPlugin also adds a switch to scalacOptions in the Compile config
  // to load the plugin.
  // The bit below removes all switches that could be passed to scalac about WartRemover during a non-lint compile.
  scalacOptions in Compile := (scalacOptions in Compile).value filterNot { switch =>
    switch.startsWith("-P:linter:") ||
      "^-Xplugin:.*/com[.]foursquare[.]lint/.*linter.*[.]jar\$".r.pattern.matcher(switch).find
  }
}
  

// fork a new JVM for run and test:run
fork := true

// Aliases
addCommandAlias("cleanall", ";clean;clean-files")

initialCommands in console :=
  """
    |import java.util.Date
  """.stripMargin

