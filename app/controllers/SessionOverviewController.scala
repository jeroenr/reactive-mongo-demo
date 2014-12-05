package controllers

import play.api.mvc.Action

// TODO: Would be great to get rid of this
import play.api.libs.concurrent.Execution.Implicits.defaultContext
/**
 * Created by jero on 05/12/14.
 */
object SessionOverviewController extends TransactionController with TariffController{
  def listAsCsv = Action.async { request =>
    allTransactionsAsMap.map { transactions =>
      Ok(views.txt.transactions.transactionsAsCsv(transactions)).as("text/csv")
    }
  }
}
