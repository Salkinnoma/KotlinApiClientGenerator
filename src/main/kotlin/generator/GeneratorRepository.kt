package com.salkinnoma.kotlinApiClientGenerator.generator

import kotlinx.serialization.json.JsonElement

class GeneratorRepository(
    private val jsonInspector: JsonInspector,
    private val dtoGenerator: DtoGenerator
) {
    fun generateDTO(json: JsonElement, name: String) {
        val path = "build/generated/ApiClientGenerator/$name"
        val mapping = jsonInspector.inspectJsonElement(json)
        dtoGenerator.generateDTO(mapping, name, path)
    }
}