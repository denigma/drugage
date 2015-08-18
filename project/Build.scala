import com.typesafe.sbt.web.pipeline.Pipeline
import playscalajs.{PlayScalaJS,ScalaJSPlay}
import playscalajs.PlayScalaJS.autoImport._
import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.gzip.Import._
import com.typesafe.sbt.web.{PathMapping, SbtWeb}
import com.typesafe.sbt.web.SbtWeb.autoImport._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import play.twirl.sbt._
import sbt.Keys._
import sbt._
import spray.revolver.RevolverPlugin._
import com.typesafe.sbt.gzip.Import.gzip

object Build extends sbt.Build {


	val scalaJSDevStage  = Def.taskKey[Pipeline.Stage]("Apply fastOptJS on all Scala.js projects")

	def scalaJSDevTaskStage: Def.Initialize[Task[Pipeline.Stage]] = Def.task { mappings: Seq[PathMapping] =>
		mappings ++ PlayScalaJS.devFiles(Compile).value ++ PlayScalaJS.sourcemapScalaFiles(fastOptJS).value
	}
  
	// settings for all the projects
	lazy val commonSettings = Seq(
    scalaVersion := Versions.scala,
	  organization := "org.denigma",
		resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases"), // for scala-js-binding
		resolvers += sbt.Resolver.bintrayRepo("rmihael", "maven"), // for
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

	lazy val drugAge = crossProject
		.crossType(CrossType.Full)
		.in(file("app"))
		.settings(commonSettings: _*)
		.settings(
			name := "drugage",
			libraryDependencies ++= Dependencies.shared.value
		)
		.jsSettings(
			persistLauncher in Compile := true,
			persistLauncher in Test := false,
			jsDependencies += RuntimeDOM % "test",
			libraryDependencies ++= Dependencies.sjsLibs.value
		)
		.jsConfigure(p=>p.enablePlugins(ScalaJSPlay))
		.jvmConfigure(p=>p.enablePlugins(SbtTwirl,SbtWeb))
		.jvmSettings(Revolver.settings:_*)
		.jvmSettings(
			libraryDependencies ++=
				Dependencies.akka.value ++
					Dependencies.webjars.value ++
					Dependencies.rdf.value ++
					Dependencies.otherJVM.value,
			mainClass in Compile :=Some("org.denigma.drugage.Main"),
			mainClass in Revolver.reStart := Some("org.denigma.drugage.Main"),
			resolvers += "Bigdata releases" at "http://systap.com/maven/releases/",
			resolvers += "apache-repo-releases" at "http://repository.apache.org/content/repositories/releases/",
			resolvers += "nxparser-repo" at "http://nxparser.googlecode.com/svn/repository/",
			dependencyOverrides += "org.apache.lucene" % "lucene-core" % Versions.bigdataLuceneVersion, //bigdata uses outdated lucene :_(
			dependencyOverrides += "org.apache.lucene" % "lucene-analyzers" % Versions.bigdataLuceneVersion, //bigdata uses outdated lucene
			scalaJSDevStage := scalaJSDevTaskStage.value,
			//pipelineStages := Seq(scalaJSProd,gzip),
			(emitSourceMaps in fullOptJS) := true,
			pipelineStages in Assets := Seq(scalaJSDevStage,gzip), //for run configuration
			(managedClasspath in Runtime) += (packageBin in Assets).value //to package production deps
		)

	lazy val drugAgeJS = drugAge.js
	lazy val drugAgeJVM = drugAge.jvm settings( scalaJSProjects := Seq(drugAgeJS))

	lazy val root = Project("root",file("."),settings = commonSettings)
		.settings(
			mainClass in Compile := (mainClass in drugAgeJVM in Compile).value,
			(managedClasspath in Runtime) += (packageBin in drugAgeJVM in Assets).value
		) dependsOn drugAgeJVM aggregate(drugAgeJVM, drugAgeJS)
}
