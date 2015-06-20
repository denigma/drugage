package org.denigma.drugage.routes

import akka.http.extensions.stubs._
import akka.http.scaladsl.server.{Route, Directives}


class Router extends Directives {
  val sessionController: SessionController = new InMemorySessionController
  val loginController: FutureLoginController = new InMemoryLoginController

  def routes: Route = new Head().routes ~
    new Registration(
      loginController.loginByName,
      loginController.loginByEmail,
      loginController.register,
      sessionController.withToken)
      .routes ~
    new Pages().routes

}
