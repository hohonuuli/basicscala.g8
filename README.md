# basicscala.g8

## About

This repo contains a [giter8](https://github.com/n8han/giter8/) template for generating a basic scala [sbt](http://www.scala-sbt.org/) project. To use run `g8 hohonuuli/basicscala.g8` or, if you're using SBT > 0.13.13, ` sbt new hohonuuli/basicscala.g8`

This generates a project for a standalone app that:
- is built with [sbt-pack](https://github.com/xerial/sbt-pack)
- formats code on save via [scalafmt](http://scalafmt.org/)
- uses [slf4j](http://www.slf4j.org/) for logging
- uses Typesafe's [config](https://github.com/typesafehub/config) library for configuration.

## Details

The build is targeted at Java 8. The following dependencies are included

- [junit](http://junit.org/)
- [scalatest](http://www.scalatest.org/)
- [slf4j](http://www.slf4j.org/)
- [config](https://github.com/typesafehub/config)

There are also an number of sbt plugins included by default:

- [sbt-dependency-graph](https://github.com/jrudolph/sbt-dependency-graph)
- [sbt-pack](https://github.com/xerial/sbt-pack)
- [sbt-updates](https://github.com/rtimush/sbt-updates)
- [sbt-versions](https://github.com/sksamuel/sbt-versions)

Details about the usage of these plugins will be found in the generated _README.md_ file in your project