package controllers

import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import services.dal.TransactionService
import services.json.JsonActions._

import services.json.MarshallableImplicits._

// TODO: Would be great to get rid of this
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by jero on 04/12/14.
 */

object TransactionController extends TransactionController {
  def create = JsonPostAction("transaction") { jsonObject =>
    transactionService.insert(jsonObject).map { either =>
      either match {
        case Right(_) => Created(Map("errors" -> Seq()).toJson)
        case Left(exc) => InternalServerError(wrapExceptionInJson(exc))
      }
    }
  }

  def list = JsonGetAction { request =>
    allTransactionsAsMap.map { transactions =>
      Ok(Map("transactions" -> (transactions.map(_ - "_id"))).toJson)
    }
  }
}

trait TransactionController extends Controller with MongoController {
  val transactionService = TransactionService(db)

  def allTransactionsAsMap = {
    transactionService.findAll[Map[String, AnyRef]]
  }
}
