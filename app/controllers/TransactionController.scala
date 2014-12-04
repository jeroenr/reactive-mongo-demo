package controllers

import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.iteratee.Done
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by jero on 04/12/14.
 */
object TransactionController extends Controller with MongoController {
  def collection: JSONCollection = db.collection[JSONCollection]("transactions")

  def index = Action { Ok("works")}

  def create = Action.async(parse.json) { request =>
    import play.api.libs.json.Reads._
    /*
     * request.body is a JsValue.
     * There is an implicit Writes that turns this JsValue as a JsObject,
     * so you can call insert() with this JsValue.
     * (insert() takes a JsObject as parameter, or anything that can be
     * turned into a JsObject using a Writes.)
     */
    val transformer: Reads[JsObject] =
      Reads.jsPickBranch[JsString](__ \ "customerId") and
        Reads.jsPickBranch[JsNumber](__ \ "volume") reduce

    request.body.transform(transformer).map { result =>
      collection.insert(result).map { lastError =>
        Logger.debug(s"Successfully inserted with LastError: $lastError")
        Created
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }
}
