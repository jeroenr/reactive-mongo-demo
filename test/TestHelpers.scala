import play.api.test.FakeApplication

trait TestConfig {
  val dbConfig = Map(
    "mongodb.uri" -> "mongodb://admin:12345@localhost:12345/transactions"
  )

  def FakeAppWithTestDb = FakeApplication(additionalConfiguration = dbConfig)
}