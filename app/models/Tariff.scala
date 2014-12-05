package models

/**
 * Created by jero on 05/12/14.
 */
case class CurrentFee(startFee: Double, hourlyFee: Double, feePerKwh: Double)

case class Tariff(startFee: Double, hourlyFee: Double, feePerKwh: Double, activeStarting: java.util.Date)

object JsonFormats {
  import play.api.libs.json.Json
  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val tariffFormat = Json.format[Tariff]
}