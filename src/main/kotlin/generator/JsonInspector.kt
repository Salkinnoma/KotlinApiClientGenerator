package com.salkinnoma.kotlinApiClientGenerator.generator

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator
import kotlin.collections.map
import kotlin.reflect.KClass

class JsonInspector {

    fun inspectJsonElement(jsonElement: JsonElement): Any {
        return when (jsonElement) {
            is JsonObject -> {
                val typeMapping = mutableMapOf<String, Any>()
                for ((key, value) in jsonElement) {
                    typeMapping[key] = inspectJsonElement(value)
                }
                typeMapping // Returns a Map<String, Any>
            }

            is JsonArray -> {
                val elementTypes = jsonElement.map { inspectJsonElement(it) }
                if (elementTypes.distinct().size == 1) {
                    // If all elements have the same structure, return a single representative type
                    listOf(elementTypes.first())
                } else {
                    // If elements have mixed structures, return a list of individual maps
                    elementTypes
                }
            }

            is JsonPrimitive -> determineType(jsonElement)
        }
    }

    private fun determineType(jsonPrimitive: JsonPrimitive): KClass<*> = when {
        jsonPrimitive.isString -> {
            String::class
        }

        jsonPrimitive.booleanOrNull != null -> {
            Boolean::class
        }

        jsonPrimitive.content.matches(INT_REGEX) -> {
            Int::class
        }

        jsonPrimitive.content.matches(DOUBLE_REGEX) -> {
            Double::class
        }

        else -> {
            throw IllegalArgumentException("Type not Supported ${jsonPrimitive.content}")
        }
    }

    companion object{
        private val INT_REGEX = Regex("""^-?\d+$""")
        private val DOUBLE_REGEX = Regex("""^-?\d+\.\d+(?:E-?\d+)?$""")
    }
}