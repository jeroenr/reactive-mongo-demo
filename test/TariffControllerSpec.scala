import com.github.athieriot.EmbedConnection
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.Json

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class TariffControllerSpec extends Specification with EmbedConnection with TestConfig{
  sequential

  "TariffController" should {

      "handle correct Tariff message" in {
        running(FakeAppWithTestDb) {
          val json = Json.obj(
            "startFee" -> .2,
            "hourlyFee" -> 1.0,
            "feePerKwh" -> .25,
            "activeStarting" -> "2013-01-01T00:00:00Z"
          )
          val transactions = {
            route(FakeRequest(POST, "/tariffs").withJsonBody(json)).get
          }

          status(transactions) must equalTo(CREATED)
          contentType(transactions) must beSome.which(_ == "application/json")
          val noErrorsJson = Json.obj("errors" -> Json.arr())
          contentAsJson(transactions) must equalTo(noErrorsJson)
        }
      }
  }
}
