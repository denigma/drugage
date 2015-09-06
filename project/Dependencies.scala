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
		"org.querki" %%% "jquery-facade" % Versions.jqueryFacade, //scalajs facade for jQuery + jQuery extensions

		"org.denigma" %%% "semantic-ui-facade" % Versions.semanticUIFacade,

		"org.denigma" %%% "semantic-controls" % Versions.semanticControls  excludeAll ExclusionRule(organization = "com.github.inthenow")

	))

	// dependencies on javascript libs
	lazy val webjars= Def.setting(Seq(

		"org.webjars" % "Semantic-UI" % Versions.semanticUI
	))

	// common purpose libs
	lazy val shared = Def.setting(Seq(
		"com.squants"  %% "squants"  % Versions.squants,

		"org.denigma" %%% "binding-controls" % Versions.bindingControls

	))

	lazy val rdf= Def.setting(Seq(
		//"org.w3" %% "banana-bigdata" % Versions.bananaBigdata excludeAll ExclusionRule(organization = "com.github.inthenow"),

		"org.w3" %% "banana-sesame" % Versions.bananaRdf excludeAll ExclusionRule(organization = "com.github.inthenow")
	))

	lazy val otherJVM: Def.Initialize[Seq[ModuleID]]  = Def.setting(Seq(
	))

}
