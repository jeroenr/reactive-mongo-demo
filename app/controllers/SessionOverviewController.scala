package controllers

import play.api.mvc.Action
import services.business.ChargeSessionsService

import scala.concurrent.Future

// TODO: Would be great to get rid of this
import play.api.libs.concurrent.Execution.Implicits.defaultContext
/**
 * Created by jero on 05/12/14.
 */
object SessionOverviewController extends TransactionController with TariffController{
  def listTransactionsAsCsv = Action.async { request =>
    allTransactionsAsMap.map { transactions =>
      Ok(views.txt.transactions.transactionsAsCsv(transactions)).as("text/csv")
    }
  }

  def chargeSessionsOverviewAsCsv = Action.async { request =>
    val result = for {
      transactions <- allTransactionsAsMap
      maybeTariff <- getActivatedTariff
    } yield {
      transactions.map { transaction =>
        transaction + ("total" -> ChargeSessionsService.calculateTotalFee(transaction, maybeTariff.getOrElse(EMPTY_CURRENT_FEE)))
      }
    }
    result.map { transactions =>
      Ok(views.txt.transactions.chargeSessionsAsCsv(transactions)).as("text/csv")
    }
  }
}
