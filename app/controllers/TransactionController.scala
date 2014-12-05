package controllers

import play.api._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import services.json.JsonActions._

import services.json.MarshallableImplicits._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by jero on 04/12/14.
 */

object TransactionController extends Controller with MongoController {
  def collection: JSONCollection = db.collection[JSONCollection]("transactions")

  def create = JsonPostAction("mySchema") { jsonObject =>
    collection.insert(jsonObject).map { lastError =>
      Logger.debug(s"Successfully inserted with LastError: $lastError")
      if(lastError.ok)
        Created(Map("errors" -> Seq()).toJson)
      else
        InternalServerError(Map("errors" -> Seq(lastError.err.get)).toJson)
    }
  }

  def list = JsonGetAction { request =>
    // let's do our query

    val cursor = collection.find(Json.obj()).cursor[JsValue]

    // gather all the JsObjects in a list
    val futureTransactionsList = cursor.collect[List]()

    // everything's ok! Let's reply with the array
    futureTransactionsList.map { transactions =>
      Ok(Map("transactions" -> transactions.map(_.toString().toMapOf[AnyRef]() - "_id")).toJson)
    }
  }
}
