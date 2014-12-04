package services.json

import com.fasterxml.jackson.databind.JsonNode
import com.github.fge.jsonschema.main.JsonSchema
import com.github.fge.jsonschema.main.JsonSchemaFactory
import collection.JavaConversions._

import MarshallableImplicits._

/**
 * Created by jeroen on 4/2/14.
 */
object JsonValidationResult {
  def apply(success: Boolean, errors: Iterable[JsonNode]) = if(success) ValidJson else InvalidJson(errors)
}

sealed trait JsonValidationResult

case object ValidJson extends JsonValidationResult
case class InvalidJson(errors: Iterable[JsonNode]) extends JsonValidationResult

object JsonValidator {
  val schemaFactory = JsonSchemaFactory.byDefault()

  def validateJson(schemaAsString: String, jsonString: String): JsonValidationResult = {
    val schemaAsJsonNode = schemaAsString.fromJson[JsonNode]
    val jsonSchema = schemaFactory.getJsonSchema(schemaAsJsonNode)
    validateJson(jsonSchema, jsonString)
  }

  def validateJsonFromRemoteSchema(schemaUrl: String, jsonString: String): JsonValidationResult = {
    val jsonSchema = schemaFactory.getJsonSchema(schemaUrl)
    validateJson(jsonSchema, jsonString)
  }

  private def validateJson(jsonSchema: JsonSchema, jsonString: String): JsonValidationResult = {
    val json = jsonString.fromJson[JsonNode]
    val report = jsonSchema.validate(json)
    JsonValidationResult(report.isSuccess, report.map(_.asJson))
  }
}
