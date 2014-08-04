package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._

object Registration extends Controller {

  def newReg = Action {
    Ok(views.html.registration.newRegister(newPersonForm))
  }

  def create = TODO

}
