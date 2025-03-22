package com.salkinnoma.kotlinApiClientGenerator.generator

import kotlinx.serialization.json.*
import java.io.File
import kotlin.reflect.KClass

class GeneratorRepository {
    fun generateDTO(json: JsonElement, name: String) {
        val mapping = inspectJsonElement(json)
        generateDTO(mapping, name)
    }

    fun generateDTO(mapping: Any, name: String, indent: String = "", outputDir: String = "build/generated/ApiClientGenerator") {
        when (mapping) {
            is Map<*, *> -> {
                val classDef = generateClassFromMap(mapping, name, indent)
                println(classDef) // Print the generated class definition
                saveDTO(classDef, outputDir, name)
            }

            is List<*> -> {
                val listType = if (mapping.isNotEmpty()) {
                    getClassName(mapping.firstOrNull())
                } else {
                    "Unknown"
                }
                println("$indent[List of $listType]")
                // Handle the first element of the list recursively
                mapping.firstOrNull()?.let { generateDTO(it, name, "$indent  ") }
            }

            else ->
                // Handle primitive types (Int, String, etc.)+
                println("$indent$name -> ${getClassName(mapping)}")
        }
    }

    private fun saveDTO(dto: String, dir: String, name: String){
        val dir = File(dir)
        if (!dir.exists()) {
            dir.mkdirs() // Create directories if they don't exist
        }
        val fileName = "${name}DTO.kt"
        val file = File(dir, fileName)
        file.appendText(dto)
    }

    fun generateClassFromMap(mapping: Map<*, *>, name: String, indent: String): String {
        val properties = mutableListOf<String>()
        mapping.forEach { (key, value) ->
            val propertyType = when (value) {
                is Map<*, *> -> {
                    // If the value is a Map, generate a nested class
                    val nestedClassName = "${name}${key.toString().capitalize()}"
                    generateDTO(value, nestedClassName, "$indent  ") // Generate the nested class recursively
                    nestedClassName
                }

                is List<*> -> {
                    // If it's a List, generate the class for the first element
                    if (value.isNotEmpty() && value.first() is Map<*, *>) {
                        val listClassName = "${name}${key.toString().capitalize()}"
                        generateDTO(value.first() as Map<*, *>, listClassName, "$indent  ")
                        "List<$listClassName>"
                    } else
                    // List of primitives
                        "List<${getClassName(value.firstOrNull())}>"
                }

                else ->
                    // For primitive types
                    getClassName(value)
            }
            properties.add("    val $key: $propertyType")
        }

        return "import kotlinx.serialization.Serializable\n\n@Serializable\ndata class ${name}DTO(\n${properties.joinToString(",\n")}\n)"
    }

    fun getClassName(value: Any?): String {
        return when (value) {
            is String -> "String"
            is Int -> "Int"
            is Long -> "Long"
            is Boolean -> "Boolean"
            is Double -> "Double"
            is Float -> "Float"
            is List<*> -> "List<${getClassName(value.firstOrNull())}>"
            is Map<*, *> -> "Map<String, Any>"
            is KClass<*> -> value.simpleName ?: "Unknown" // Handle KClass (e.g., Int::class)
            else -> value?.javaClass?.simpleName ?: "Any"
        }
    }

    private fun inspectJsonElement(jsonElement: JsonElement): Any {
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

    companion object {
        private val INT_REGEX = Regex("""^-?\d+$""")
        private val DOUBLE_REGEX = Regex("""^-?\d+\.\d+(?:E-?\d+)?$""")
    }
}