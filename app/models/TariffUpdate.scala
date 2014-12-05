package models

/**
 * Created by jero on 05/12/14.
 */
case class CurrentTariff(startFee: Double, hourlyFee: Double, feePerKwh: Double)

case class TariffUpdate(startFee: Double, hourlyFee: Double, feePerKwh: Double, activeStarting: java.util.Date)

object JsonFormats {
  import play.api.libs.json.Json
  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val tariffUpdateFormat = Json.format[TariffUpdate]
  implicit val currentTariffFormat = Json.format[CurrentTariff]
}