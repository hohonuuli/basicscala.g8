# basicscala.g8

## About

This repo contains a [giter8](https://github.com/n8han/giter8/) template for generating a basic scala [sbt](http://www.scala-sbt.org/) project. To use run `g8 hohonuuli/basicscala.g8` and you will be prompted for the usual:

- __organization__ for project
- __name__ of project
- __version__
- __scala version__ for the project

This template implements relatively strict style and coding standards based on the recommendations in the [Toward a Safer Scala](https://docs.google.com/presentation/d/1tCmphnyP3F5WUtd1iNLub0TWRVyfTJsqhvzYhNS41WY/pub?start=false#slide=id.p) talk.

## Details

The build is targeted at Java 8. The following dependencies are included

- [junit](http://junit.org/)
- [scalatest](http://www.scalatest.org/)
- [slf4j](http://www.slf4j.org/)

There are also an number of sbt plugins included by default:

- [sbt-dependency-graph](https://github.com/jrudolph/sbt-dependency-graph)
- [sbt-pack](https://github.com/xerial/sbt-pack)
- [sbt-scalariform](https://github.com/sbt/sbt-scalariform)
- [sbt-updates](https://github.com/rtimush/sbt-updates)
- [sbt-versions](https://github.com/sksamuel/sbt-versions)
- [sbt-wartremover](https://github.com/puffnfresh/wartremover)
- [scalastyle-sbt-plugin](http://www.scalastyle.org/sbt.html)

Details about the usage of these plugins will be found in the generated _README.md_ file in your project