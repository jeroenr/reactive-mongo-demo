package services.dal

import play.api.Logger
import play.api.libs.json.{Json, JsValue, JsObject}
import play.modules.reactivemongo.json.collection.JSONCollection
import services.json.MarshallableImplicits._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by jero on 05/12/14.
 */
class TransactionService(db: reactivemongo.api.DefaultDB, collectionName: String) {
  def collection: JSONCollection = db.collection[JSONCollection](collectionName)

  def insert(jsObject: JsObject) = collection.insert(jsObject).map { lastError =>
    Logger.debug(s"Inserted with LastError: $lastError")
    if(lastError.ok)
      Right(jsObject)
    else
      Left(new RuntimeException(s"Failed to insert: ${lastError.errMsg.get}"))

  }

  def findAll[T]()(implicit m : Manifest[T]) = find[T](Json.obj())

  def find[T](query: JsObject)(implicit m : Manifest[T]) = {
    val cursor = collection.find(query).cursor[JsValue]
    cursor.collect[List]().map(_.map(_.toString().fromJson[T]))
  }
}

object TransactionService {
  def apply(db: reactivemongo.api.DefaultDB): TransactionService = new TransactionService(db, "transactions")
}
