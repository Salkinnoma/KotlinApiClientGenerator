package com.salkinnoma.kotlinApiClientGenerator.setupRequest

import kotlinx.serialization.json.JsonElement

class SetupRequestRepository(
    private val setupRequestClient: SetupRequestClient,
    private val setupRequestParser: SetupRequestParser
) {
    fun getAndParseRequest(baseurl: String, query: String?): JsonElement? {
        val response = setupRequestClient.doRequest(baseurl, query)
        return setupRequestParser.parseRequest(response)
    }
}