package org.denigma.drugage.routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{Directives, Route}
import org.denigma.drugage.templates.MyStyles

import scalacss.Defaults._

class Head extends Directives
{

  protected lazy val webjarsPrefix = "lib"
  protected lazy val resourcePrefix = "resources"

  protected def mystyles = path("styles" / "mystyles.css"){
    complete  {
      HttpResponse(entity = HttpEntity(MediaTypes.`text/css`, MyStyles.render))
      }
    }

  protected def loadResources = pathPrefix(resourcePrefix~Slash) {
    getFromResourceDirectory("")
  }


  protected def webjars = pathPrefix(webjarsPrefix ~ Slash)  {  getFromResourceDirectory(webjarsPrefix)  }

  def routes: Route = mystyles ~ webjars ~ loadResources
}
