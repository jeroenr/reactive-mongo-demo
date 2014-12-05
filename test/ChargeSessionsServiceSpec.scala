import models.CurrentTariff
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import services.business.ChargeSessionsService

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ChargeSessionsServiceSpec extends Specification{

  "ChargeSessionsService" should {

      "calculate total session fee" in {
        val transaction = Map[String,AnyRef](
          "volume" -> "10.00",
          "startTime" -> "2014-10-28T09:34:17",
          "endTime" -> "2014-10-29T09:34:17",
          "customerId" -> "john"
        )
        ChargeSessionsService.calculateTotalFee(transaction, CurrentTariff(1d, 1d, 1d)) must equalTo(1 + 24 + 10)
      }

      "calculate session fee for incomplete transaction" in {
        val transaction = Map[String,AnyRef](
          "volume" -> "10.00",
          "customerId" -> "john"
        )
        ChargeSessionsService.calculateTotalFee(transaction, CurrentTariff(1d, 1d, 1d)) must equalTo(1 + 0 + 10)
      }
  }
}
