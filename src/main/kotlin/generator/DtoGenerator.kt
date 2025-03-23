package com.salkinnoma.kotlinApiClientGenerator.generator

import java.io.File
import kotlin.reflect.KClass

class DtoGenerator {

    fun generateDTO(
        mapping: Any,
        name: String,
        outputDir: String,
        indent: String = "",
    ) {
        when (mapping) {
            is Map<*, *> -> {
                val classDef = generateClassFromMap(mapping, name, indent, outputDir)
                saveDTO(classDef, outputDir, name)
            }

            is List<*> -> {
                mapping.firstOrNull()?.let { generateDTO(it, name, outputDir, "$indent  ") }
            }
        }
    }

    fun generateClassFromMap(mapping: Map<*, *>, name: String, indent: String, outputDir: String): String {
        val properties = mutableListOf<String>()
        mapping.forEach { (key, value) ->
            val propertyType = when (value) {
                is Map<*, *> -> {
                    // If the value is a Map, generate a nested class
                    val nestedClassName = "${name}${key.toString().capitalize()}"
                    generateDTO(value, nestedClassName, outputDir, "$indent  ") // Generate the nested class recursively
                    "${nestedClassName}DTO"
                }

                is List<*> -> {
                    // If it's a List, generate the class for the first element
                    if (value.isNotEmpty() && value.first() is Map<*, *>) {
                        val listClassName = "${name}${key.toString().capitalize()}"
                        generateDTO(value.first() as Map<*, *>, listClassName, outputDir, "$indent  ")
                        "List<${listClassName}DTO>"
                    } else
                    // List of primitives
                        "List<${getClassName(value.firstOrNull())}DTO>"
                }

                else ->
                    // For primitive types
                {
                    getClassName(value)
                }
            }
            properties.add("    val $key: $propertyType")
        }

        val classHeader = "data class ${name}DTO("
        val propertiesBlock = properties.joinToString(",\n")
        val classFooter = ")"


        return listOf(
            SERIALIZABLE_IMPORT,
            DO_NOT_EDIT,
            SERIALIZABLE_ANNOTATION,
            classHeader,
            propertiesBlock,
            classFooter
        ).joinToString("\n")
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

    private fun saveDTO(dto: String, dir: String, name: String) {
        val dir = File(dir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val fileName = "${name}DTO.kt"
        val file = File(dir, fileName)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.appendText(dto)
    }

    companion object {
        private const val SERIALIZABLE_IMPORT = "import kotlinx.serialization.Serializable"
        private const val SERIALIZABLE_ANNOTATION = "@Serializable"
        private const val DO_NOT_EDIT = "// WARNING: Autogenerated: Do not edit \n" +
                "// Any changes made will be overwritten during the next code generation cycle."
    }
}