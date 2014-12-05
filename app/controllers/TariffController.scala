package controllers

import java.util.Date

import models.{CurrentTariff, TariffUpdate}
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import services.dal.TariffService
import services.json.JsonActions._

import services.json.MarshallableImplicits._

// TODO: Would be great to get rid of this
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.JsonFormats._
/**
 * Created by jero on 05/12/14.
 */
object TariffController extends Controller with MongoController {
  val tariffService = TariffService(db)

  val EMPTY_CURRENT_FEE = CurrentTariff(0,0,0)

  def create = JsonPostAction("tariff") { tariff =>
    tariffService.insert(tariff.toString.fromJson[TariffUpdate]).map { either =>
      either match {
        case Right(_) => Created(Map("errors" -> Seq()).toJson)
        case Left(exc) => InternalServerError(wrapExceptionInJson(exc))
      }
    }
  }

  def currentTariff = JsonGetAction { request =>
    tariffService.findMostRecent[CurrentTariff](activatedTariffsQuery).map { maybeTariff =>
      maybeTariff match {
        case Some(tariff) => Ok(tariff.toJson)
        case _ => Ok(EMPTY_CURRENT_FEE.toJson)
      }
    }
  }

  def activatedTariffsQuery = {
    Json.obj("activeStarting" -> Json.obj("$lte" -> new Date().getTime))
  }
}
