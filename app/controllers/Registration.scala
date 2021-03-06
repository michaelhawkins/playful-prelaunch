package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._
import models._
import play.api.data._
import play.api.data.Forms._
import services.{Message}

object Registration extends Controller {

  val newPersonForm: Form[NewPerson] = Form(
    mapping(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "email" -> nonEmptyText(minLength = 6) //shortest domain is k.st add @ + 1 letter and the min email length is 6
    )(NewPerson.apply)(NewPerson.unapply)
  )


  def newReg = Action {
    Ok(views.html.registration.newReg(newPersonForm))
  }

  def create = Action { implicit request =>
    newPersonForm.bindFromRequest.fold(
      errors => BadRequest(views.html.registration.newReg(errors)),
      person => {
        val newPerson = Person.create(person.firstName, person.lastName,
          person.email)
        Message.emailNewRegistration(newPerson.firstName, newPerson.lastName, newPerson.email)
        Ok(views.html.registration.registrationSuccess(newPerson.firstName, newPerson.email))
      }
    )
  }

}
