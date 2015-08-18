package org.denigma.drugage.routes

import akka.http.extensions.security.LoginInfo
import akka.http.extensions.stubs._
import akka.http.scaladsl.server.{Route, Directives}


class Router extends Directives {
  val sessionController: SessionController = new InMemorySessionController
  val loginController: InMemoryLoginController = new InMemoryLoginController()
  loginController.addUser(LoginInfo("admin", "test2test", "test@email"))

  def routes: Route = new Head().routes ~
    new Registration(
      loginController.loginByName,
      loginController.loginByEmail,
      loginController.register,
      sessionController.userByToken,
      sessionController.makeToken
    )
      .routes ~
    new Pages().routes

}
