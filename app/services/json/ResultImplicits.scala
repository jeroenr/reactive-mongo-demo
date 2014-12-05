package services.json

import play.api.mvc.{Request, Result}

/**
 * Created by jeroen on 2/18/14.
 */
object ResultImplicits {
  val JSON_CONTENT_TYPE: String = "application/json"

  implicit class MyResult(result: Result){
    def asJsonWithAccessControlHeaders(implicit request: Request[Any]): Result = {
      result.as(JSON_CONTENT_TYPE).withHeaders(
        "Access-Control-Allow-Origin" -> request.headers.get("Origin").getOrElse("*"),
        "Access-Control-Allow-Credentials" -> "true"
      )
    }
  }

}