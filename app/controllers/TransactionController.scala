package controllers

import play.api._
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import services.json.JsonActions._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by jero on 04/12/14.
 */

object TransactionController extends Controller with MongoController {
  def collection: JSONCollection = db.collection[JSONCollection]("transactions")

  def create = JsonPostAction("mySchema") { jsonObject =>
    collection.insert(jsonObject).map { lastError =>
      Logger.debug(s"Successfully inserted with LastError: $lastError")
      Created
    }
  }
}
