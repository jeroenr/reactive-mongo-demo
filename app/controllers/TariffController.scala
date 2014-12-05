package controllers

import models.Tariff
import play.api._
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import services.dal.TariffService
import services.json.JsonActions._

import services.json.MarshallableImplicits._

// TODO: Would be great to get rid of this
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by jero on 05/12/14.
 */
object TariffController extends Controller with MongoController {
  val tariffService = TariffService(db)

  def create = JsonPostAction("tariff") { jsonObject =>
    tariffService.insert(jsonObject).map { either =>
      either match {
        case Right(_) => Created(Map("errors" -> Seq()).toJson)
        case Left(exc) => InternalServerError(wrapExceptionInJson(exc))
      }
    }
  }

  def currentTariff = JsonGetAction { request =>
    tariffService.findMostRecent[Tariff].map { maybeTariff =>
      maybeTariff match {
        case Some(tariff) => Ok(tariff.toJson)
        case _ => Ok(Tariff(0,0,0).toJson)
      }
    }
  }
}
