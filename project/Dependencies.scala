import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._


object Dependencies {

	// libs for testing
  lazy val testing = Def.setting(Seq(
    "org.scalatest" %%% "scalatest" % Versions.scalatest % "test"
  ))

	// akka-related libs
	lazy val akka = Def.setting(Seq(
		"org.denigma" %%% "akka-http-extensions" % Versions.akkaHttpExtensions,

		"com.typesafe.akka" %% "akka-http-testkit-experimental" % Versions.akkaHttp
	))

	// scalajs libs
	lazy val sjsLibs= Def.setting(Seq(
		"org.querki" %%% "jquery-facade" % Versions.jqueryFacade //scalajs facade for jQuery + jQuery extensions
	))

	// dependencies on javascript libs
	lazy val webjars= Def.setting(Seq(

		"org.webjars" % "Semantic-UI" % Versions.semanticUI, // css theme, similar to bootstrap

		"org.webjars" % "selectize.js" % Versions.selectize // select control
	))

	// common purpose libs
	lazy val shared = Def.setting(Seq(
		"com.softwaremill.quicklens" %%% "quicklens" % Versions.quicklens, // nice lenses for case classes

	  "com.squants"  %% "squants"  % "0.5.3",	// library for modelling physical quantities

		"org.denigma" %%% "binding-controls" % Versions.bindingControls,

		"com.github.japgolly.scalacss" %%% "core" % Versions.scalaCSS,

		"com.github.japgolly.scalacss" %%% "ext-scalatags" % Versions.scalaCSS

	))

	lazy val rdf = Def.setting(Seq(
		//"org.w3" %% "banana-bigdata" % Versions.bananaBigdata excludeAll ExclusionRule(organization = "com.github.inthenow"),

		"org.w3" %% "banana-sesame" % Versions.bananaRdf
	))

}
