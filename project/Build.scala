import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.web.SbtWeb
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import spray.revolver.RevolverPlugin._
import play.twirl.sbt._
import play.twirl.sbt.SbtTwirl.autoImport._
import com.typesafe.sbt.web.SbtWeb.autoImport._


object Build extends sbt.Build {
  
	// settings for all the projects
	lazy val commonSettings = Seq(
    scalaVersion := Versions.scala,
	  organization := "org.denigma",
		resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases"), // for scala-js-binding
    libraryDependencies ++= Dependencies.shared.value++Dependencies.testing.value,
		updateOptions := updateOptions.value.withCachedResolution(true), // to speed up dependency resolution
		scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
  )

	// sbt-native-packager settings to run it as daemon
	lazy val packageSettings = Seq(
		maintainer := "Anton Kulaga <antonkulaga@gmail.com>",
		packageSummary:= "DrugAge database prototype",
		packageDescription := "DrugAge database prototype"
		// serverLoading in Debian := Upstart
	)

	lazy val frontend = crossProject
	  .crossType(CrossType.Full)
	  .in(file("frontend"))
	  .settings(commonSettings: _*)
	  .settings(
			name := "frontend",
			libraryDependencies ++= Dependencies.shared.value
			)
    .jsSettings(
			persistLauncher in Compile := true,
			persistLauncher in Test := false,
			jsDependencies += RuntimeDOM % "test",
			libraryDependencies ++= Dependencies.sjsLibs.value
		)

	lazy val frontendJVM = frontend.jvm // what frontend requires from the backend
	lazy val frontendJS = frontend.js // all scalajs code is here


	// backend project
	lazy val backend = Project("backend", file("backend"),settings = commonSettings++Revolver.settings)
		.settings(packageSettings:_*)
		.settings(
			libraryDependencies ++=
				Dependencies.akka.value ++
				Dependencies.webjars.value ++
				Dependencies.rdf.value,
			mainClass in Compile :=Some("org.denigma.drugage.Main"),
			mainClass in Revolver.reStart := Some("org.denigma.drugage.Main"),
			resourceGenerators in Compile <+=  (fastOptJS in Compile in frontendJS,
				packageScalaJSLauncher in Compile in frontendJS) map( (f1, f2) => Seq(f1.data, f2.data)),
			resolvers += "Bigdata releases" at "http://systap.com/maven/releases/",
			resolvers += "apache-repo-releases" at "http://repository.apache.org/content/repositories/releases/",
			resolvers += "nxparser-repo" at "http://nxparser.googlecode.com/svn/repository/",
			dependencyOverrides += "org.apache.lucene" % "lucene-core" % Versions.bigdataLuceneVersion, //bigdata uses outdated lucene :_(
			dependencyOverrides += "org.apache.lucene" % "lucene-analyzers" % Versions.bigdataLuceneVersion, //bigdata uses outdated lucene
		watchSources <++= (watchSources in frontendJS),
		(managedClasspath in Runtime) += (packageBin in Assets).value
		) enablePlugins(SbtTwirl,SbtWeb) dependsOn frontendJVM

	lazy val root = Project("root",file("."),settings = commonSettings)
		.settings(
			mainClass in Compile := (mainClass in backend in Compile).value,
			libraryDependencies += "com.lihaoyi" % "ammonite-repl_2.11.6" %  Versions.ammonite,
			initialCommands in console := """ammonite.repl.Repl.run("")""" //better console
    ) dependsOn backend aggregate(backend,frontendJS)
}
