object Versions extends WebJarsVersions with ScalaJSVersions with RdfVersions with SharedVersions
{
	val scala = "2.11.7"

	val akkaHttp = "1.0"

	val ammonite = "0.4.5"

	val akkaHttpExtensions = "0.0.5"

	val scalatest = "3.0.0-M7"

}

trait ScalaJSVersions {

	val jqueryFacade = "0.6"

	val semanticUIFacade = "0.0.1"

}

//versions for libs that are shared between client and server
trait SharedVersions
{
	val autowire = "0.2.5"

	val quicklens = "1.3.1"

	val squants = "0.6.0-drugage"

	val scalaTags = "0.5.1"

	val scalaCSS = "0.3.0"

	val productCollections = "1.4.2"

	val utest = "0.3.1"

	val bindingControls = "0.0.6"

	val binding = "0.8.0-M2"

	val semanticBinding = "0.8.0-M2"

}

trait WebJarsVersions {

	val jquery =  "2.1.4"

	val semanticUI = "2.0.7"

	val selectize = "0.12.1"

}

trait RdfVersions {

	val bananaRdf = "0.8.1"

	val sesame = "2.8.3"

	val bananaBigdata = "0.8.2-SNAP4"

	val bigdataVersion = "1.5.2"

	val bigdataSesameVersion = "2.7.13"

	val bigdataLuceneVersion = "3.0.0"
}
