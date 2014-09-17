package services
import play.api.i18n._
import play.api.libs.json._
import play.api.libs.ws._
import scala.concurrent.Future
import play.api.Play.current

object Message {

def buildRegistrationTemplate(firstName: String, lastName: String, email: String, mandrillKey: String): JsValue = {
  JsObject(Seq(
        "key" -> JsString(mandrillKey),
        "template_name" -> JsString("my-playful-prelaunch-registration"),
        "template_content" -> JsArray(Seq(
          JsObject(Seq("name" -> JsString("firstname"), "content" -> JsString(firstName))),
          JsObject(Seq("name" -> JsString("appname"), "content" -> JsString(Messages("global.appName")))))),
        "message" -> JsObject(
          Seq("to" -> JsArray(Seq(
                JsObject(Seq("email" -> JsString(email),
                "name" -> JsString(firstName + " " + lastName),
                "type" -> JsString("to")
              )))),
        "headers" -> JsObject(Seq(
          "Reply-To" -> JsString(Messages("global.replyToEmail")))),
        "important" -> JsBoolean(false),
        "track_opens" -> JsBoolean(true)))))
}

  def emailNewRegistration(firstName: String, lastName: String, email: String) {
    val mandrillKey: String = play.Play.application.configuration.getString("mandrillKey")
    val jsonClass = buildRegistrationTemplate(firstName, lastName, email, mandrillKey)

    val apiUrl = play.Play.application.configuration.getString("mandrillSendViaTemplateUrl")
    WS.url(apiUrl).post(jsonClass)
  }
}
