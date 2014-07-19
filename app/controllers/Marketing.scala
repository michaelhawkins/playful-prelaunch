package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._

object Marketing extends Controller {

  def about = Action {
    Ok(views.html.marketing.about(Messages("global.about")))
  }

  def contact = Action {
    Ok(Messages("global.contact"))
  }
}
