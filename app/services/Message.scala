package services
import play.api.i18n._
import play.api.libs.json._
import play.api.libs.ws._
import scala.concurrent.Future
import play.api.Play.current

object Message {

  def buildRegistrationTemplate(firstName: String, lastName: String, email: String, mandrillKey: String): JsValue = {
    JsObject(Seq(
    //      "key" -> JsString("5t1Q3spLNNtrBesf3gxK7A"),*/
          "key" -> JsString(mandrillKey),
          "template_name" -> JsString("my-playful-prelaunch-registration"),
          "template_content" -> JsArray(Seq(
            JsObject(Seq("name" -> JsString("firstname"), "content" -> JsString(firstName))),
            JsObject(Seq("name" -> JsString("appname"), "content" -> JsString("global.appName"))))),
          "message" -> JsObject(
            Seq("subject" -> JsString("Thank you for your interest in" + Messages("global.appName")),
              "from_email" -> JsString(Messages("global.fromEmail")),
              "from_name" -> JsString(Messages("global.companyName")),
              "to" -> JsArray(Seq(
                  JsObject(Seq("email" -> JsString(email), //this email var isn't working
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
    val jsonClass: JsValue = buildRegistrationTemplate(firstName, lastName, email, mandrillKey)

    val apiUrl: String = play.Play.application.configuration.getString("mandrillSendViaTemplateURL")
    val futureResponse: Future[WSResponse] = WS.url(apiUrl).post(jsonClass)
  }
}
