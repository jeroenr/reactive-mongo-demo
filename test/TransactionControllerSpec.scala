import com.github.athieriot.{CleanAfterExample, EmbedConnection}
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json.{JsObject, Json}

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class TransactionControllerSpec extends Specification with EmbedConnection with TestConfig{
  sequential

  "TransactionController" should {

      "send 404 on a bad request" in {
        running(FakeAppWithTestDb) {
          route(FakeRequest(GET, "/boum")) must beNone
        }
      }

      "handle correct SCPP message" in {
        running(FakeAppWithTestDb) {
          val json = Json.obj(
            "customerId" -> "john",
            "startTime" -> "2014-10-28T09:34:17",
            "endTime" -> "2014-10-28T16:45:13",
            "volume" -> 32.03
          )
          val transactions = {
            route(FakeRequest(POST, "/transactions").withJsonBody(json)).get
          }

          status(transactions) must equalTo(CREATED)
          contentType(transactions) must beSome.which(_ == "application/json")
          val noErrorsJson = Json.obj("errors" -> Json.arr())
          contentAsJson(transactions) must equalTo(noErrorsJson)
        }
      }

    "handle wrong SCPP message" in {
      running(FakeAppWithTestDb) {
        val json = Json.obj(
          "customerId" -> "john"
        )
        val transactions = {
          route(FakeRequest(POST, "/transactions").withJsonBody(json)).get
        }

        status(transactions) must equalTo(BAD_REQUEST)
        contentType(transactions) must beSome.which(_ == "application/json")
      }
    }
  }
}
