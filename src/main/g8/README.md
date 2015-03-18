# $name$

This project is built using [SBT](http://www.scala-sbt.org/)

## Useful [SBT commands](http://www.scala-sbt.org/release/docs/Command-Line-Reference.html) for this project

- __checkVersions__: Show dependency updates
- __clean__
- __cleanall__: Does __clean__ and __clean-files__
- __compile__ or __~compile__ (continuous)
- __console__: Opens a scala console that includes the projects dependencies and code on the classpath
- __dependency-graph__: Shows an ASCII dependency graph
- __dependencyUpdates__: Show dependency updates
- __doc__: Generate Scaladoc into target/api
- __export fullClasspath__: Generate the classpath needed to run the project
- __install__
- __ivy-report__: build a report of dependencies using ivy in XML (viewable in a browser)
- __lint:compile__: Run static checkers as part of compilion. (Static checking is slow)
- __offline__: Use SBT offline
- __pack__: Builds a standalone distribution of this project under __target/pack__
- __package__: Creates the main artifact (e.g. a jar) under __target__
- __publish-local__ or __~publish-local__ (continous): Publish to the local ivy repo
- __publishM2__: Publish to the local maven repo
- __reload__: Reloads the build. Useful if you edit build.sbt.
_ __scalastyleGenerateConfig: Generates a scalastyle config file. Run before using __scalastyle__
- __scalastyle__: Checks code style. Results go into target/scalastyle-result.xml. Also __test:scalastyle__
- __show ivy-report__: Show the location of the dependency report
- __show update__: Show dependencies and indicate which were evicted
- __tasks -V__: Shows all available tasks/commands
- __test__ or __~test__ (continuous)
- __update-classifiers__: Download sources and javadoc for all dependencies
- __version-report__: Shows a flat listing of all dependencies in this project, including transitive ones.

## To run main class using SBT to launch it
\`sbt 'run-main org.mbari.foo.Main' \`

## To run a single test
\`sbt 'test-only org.mbari.foo.ExampleSpec' \`

## References
[Best Practices](https://github.com/alexandru/scala-best-practices/)

