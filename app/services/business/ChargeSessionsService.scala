package services.business

import models.CurrentTariff
import org.joda.time.{Hours, LocalDateTime}
import org.joda.time.format.ISODateTimeFormat

/**
 * Created by jero on 05/12/14.
 */
object ChargeSessionsService {
  def calculateTotalFee(transaction: Map[String,AnyRef], tariff: CurrentTariff) = {
    val volume: Double = transaction.get("volume") match {
      case Some(v) if v.isInstanceOf[Double] => v.asInstanceOf[Double]
      case Some(v) if v.isInstanceOf[String] => v.asInstanceOf[String].toDouble
      case _ => 0d
    }
    val startTime = parseDate(transaction, "startTime")
    val endTime = parseDate(transaction, "endTime")
    val numHours = Hours.hoursBetween(startTime, endTime).getHours
    tariff.startFee + (numHours * tariff.hourlyFee) + (volume * tariff.feePerKwh)
  }

  private def parseDate(transaction: Map[String,AnyRef], field: String) = {
    transaction.get(field).asInstanceOf[Option[String]] match {
      case Some(dateTime) => ISODateTimeFormat.dateTimeParser().parseLocalDateTime(dateTime)
      case _ => new LocalDateTime()
    }

  }

}
