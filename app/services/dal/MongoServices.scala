package services.dal

import play.api.Logger
import play.api.libs.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.core.commands.LastError
import services.json.MarshallableImplicits._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
/**
 * Created by jero on 05/12/14.
 */
class MongoService(db: reactivemongo.api.DefaultDB, collectionName: String) {
  def collection: JSONCollection = db.collection[JSONCollection](collectionName)

  def insert[T](t: T)(implicit writes: Writes[T]) = collection.insert[T](t).map { handleInsert[T](t) }

  def insert(jsObject: JsObject) = collection.insert(jsObject).map { handleInsert[JsObject](jsObject) }

  private def handleInsert[T](t: T): (LastError) => Either[RuntimeException, T] with Product with Serializable = {
    lastError =>
      Logger.debug(s"Inserted with LastError: $lastError")
      if (lastError.ok)
        Right(t)
      else
        Left(new RuntimeException(s"Failed to insert: ${lastError.errMsg.get}"))
  }

  def findAll[T]()(implicit m : Manifest[T]) = find[T](Json.obj())

  def find[T](query: JsObject)(implicit m : Manifest[T]) = {
    val cursor = collection.find(query).cursor[JsValue]
    cursor.collect[List]().map(_.map(_.toString().fromJson[T]))
  }

  def findMostRecent[T]()(implicit m : Manifest[T]): Future[Option[T]] = findMostRecent[T](Json.obj())

  def findMostRecent[T](query: JsObject)(implicit m : Manifest[T]): Future[Option[T]] = {
    collection
      .find(query)
      .sort(Json.obj("_id" -> -1))
      .one[JsObject].map(_.map(_.toString().fromJson[T]))
  }
}

object TransactionService {
  def apply(db: reactivemongo.api.DefaultDB): MongoService = new MongoService(db, "transactions")
}

object TariffService {
  def apply(db: reactivemongo.api.DefaultDB): MongoService = new MongoService(db, "tariffs")
}
