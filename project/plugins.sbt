addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.2.0") //advanced assets handling

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2") //live refresh

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3") //packaging for production

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.4") //ScalaJS

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.1.1") //templates

addSbtPlugin("com.gilt" % "sbt-dependency-graph-sugar" % "0.7.5-1") //visual dependency management

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
