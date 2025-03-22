package com.salkinnoma.kotlinApiClientGenerator.setupRequest

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class SetupRequestParser {
    fun parseRequest(response: String): JsonElement {
        return try {
            Json.parseToJsonElement(response)
        } catch (e: Exception) {
            val exceptionMessage = "Invalid api request. ${e.stackTraceToString()}"
            throw IllegalArgumentException(exceptionMessage)
        }
    }
}