package services.json

import play.api.libs.json.{JsObject, JsValue}
import play.api.mvc.BodyParsers._
import play.api.mvc.Results._
import play.api.mvc._
import MarshallableImplicits._
import ResultImplicits._

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext


object JsonActions extends JsonActions

trait JsonActions {
  val WILDCARD_SCHEMA = """{}"""

  // TODO: read this from a file
  val schemaMap = Map(
    "transaction" -> """
                       |
                       |{
                       |    "$schema": "http://json-schema.org/draft-04/schema#",
                       |    "type": "object",
                       |    "required": ["customerId","startTime","endTime","volume"],
                       |    "properties": {
                       |        "customerId": {
                       |            "type": "string"
                       |        },
                       |        "startTime": {
                       |            "type": "string",
                       |            "pattern": "^(-?(?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.[0-9]+)?(Z|[+-](?:2[0-3]|[01][0-9]):[0-5][0-9])?$"
                       |        },
                       |        "endTime": {
                       |            "type": "string",
                       |            "pattern": "^(-?(?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.[0-9]+)?(Z|[+-](?:2[0-3]|[01][0-9]):[0-5][0-9])?$"
                       |        },
                       |        "volume": {
                       |            "anyOf": [
                       |               {
                       |                   "type": "number"
                       |               },
                       |               {
                       |                   "type": "string"
                       |               }
                       |             ]
                       |        }
                       |    },
                       |    "additionalProperties": false
                       |}
                     """.stripMargin
  )




  def JsonPostAction(jsonSchemaId: String, maxLength: Int)(f: JsObject => Future[Result]) = Action.async(parse.json(maxLength = maxLength)) {
    implicit request =>
      val jsValue: JsValue = request.body
      JsonValidator.validateJson(schemaMap.getOrElse(jsonSchemaId, WILDCARD_SCHEMA), jsValue.toString) match {
        case ValidJson =>
          f(jsValue.asInstanceOf[JsObject]).map(_.asJsonWithAccessControlHeaders)
        case InvalidJson(errors) => Future.successful(BadRequest(Map(
          "exception" -> "The request body violates the schema definition",
          "schema_uri" -> s"${request.host}/schema/${jsonSchemaId}.json",
          "errors" -> errors
        ).toJson).asJsonWithAccessControlHeaders)
      }
  }

  def JsonPostAction(maxLength: Int)(f: JsObject => Future[Result]) = Action.async(parse.json(maxLength = maxLength)) {
    implicit request =>
      val jsValue: JsValue = request.body
      f(jsValue.asInstanceOf[JsObject]).map(_.asJsonWithAccessControlHeaders)
  }

  def JsonPostAction(jsonSchemaId: String)(f: JsObject => Future[Result]): Action[JsValue] = JsonPostAction(jsonSchemaId, parse.DEFAULT_MAX_TEXT_LENGTH)(f)

  def JsonPostAction(f: JsObject => Future[Result]): Action[JsValue] = JsonPostAction(parse.DEFAULT_MAX_TEXT_LENGTH)(f)

  def JsonGetAction(f: Request[AnyContent] => Future[Result]) = Action.async {
    implicit request =>
      f(request).map(_.asJsonWithAccessControlHeaders)
  }

  def wrapExceptionInJson(t: Throwable) = Map("exception" -> t.getMessage).toJson


}
