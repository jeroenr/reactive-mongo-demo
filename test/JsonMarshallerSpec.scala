import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import services.json.JsonMarshaller

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class JsonMarshallerSpec extends Specification{

  "JsonMarshaller" should {

      "unmarshall empty JSON to Map" in {
        val json = """{}"""
        JsonMarshaller.toMap[AnyRef](json) must equalTo(Map())
      }

      "unmarshall nested JSON to Map" in {
        val json =
          """{
            | "foo" : {
            |   "bar" : 2
            | }
            |}""".stripMargin
        JsonMarshaller.toMap[AnyRef](json) must equalTo(Map("foo" -> Map("bar" -> 2)))
      }

      "marshall case class to JSON" in {
        import services.json.MarshallableImplicits._
        case class A(hello: String, bar: Int)
        val a = A("world!", 1)
        a.toJson must equalTo("""{"hello":"world!","bar":1}""")
      }
  }
}
