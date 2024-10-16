package linter

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import rules.CamelCaseRule
import rules.InputOnlyRule
import rules.PrintOnlyRule
import rules.Rule
import rules.SnakeCaseRule
import java.io.File

// Definición de reglas de formato
class FormattingRules {
    @JsonProperty("identifier_format")
    val identifier: String? = null

    @JsonProperty("enable_print_only")
    val isEnablePrintOnly: Boolean = false

    @JsonProperty("enable_input_only")
    val isEnableInputOnly: Boolean = false
}

// Clase para leer y obtener reglas desde JSON
class RuleJsonReader {
    private val mapper: ObjectMapper = jacksonObjectMapper()

    // Método para leer reglas desde un archivo JSON
    fun getRulesFromFile(path: String): List<Rule> {
        val file = File(path)
        return getRulesFromJson(file.readText()) // Lee el contenido del archivo y pasa al método que procesa el contenido
    }

    // Método para leer reglas desde un contenido JSON en formato String
    fun getRulesFromJson(jsonContent: String): List<Rule> {
        return try {
            val formattingRules = mapper.readValue(jsonContent, FormattingRules::class.java)
            val rules = mutableListOf<Rule>()

            // Normalizar y recortar el identificador para manejar variaciones como "camel case" o "snake case"
            when (formattingRules.identifier?.replace(" ", "")?.lowercase()) {
                "camelcase" -> rules.add(CamelCaseRule())
                "snakecase" -> rules.add(SnakeCaseRule())
            }

            if (formattingRules.isEnablePrintOnly) {
                rules.add(PrintOnlyRule())
            }
            if (formattingRules.isEnableInputOnly) {
                rules.add(InputOnlyRule())
            }

            rules
        } catch (e: Exception) {
            println("Error al analizar el JSON: ${e.message}")
            emptyList() // Retorna una lista vacía o puedes lanzar una excepción según tus necesidades
        }
    }
}
