package linter

import com.fasterxml.jackson.annotation.JsonProperty
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
    var identifier: String? = null

    @JsonProperty("enable_print_only")
    var isEnablePrintOnly: Boolean = false

    @JsonProperty("enable_input_only")
    var isEnableInputOnly: Boolean = false
}

// Clase para leer y obtener reglas desde JSON
class RuleJsonReader {
    // Método para leer reglas desde un archivo JSON
    fun getRulesFromFile(path: String): List<Rule> {
        val file = File(path)
        return getRulesFromJson(file.readText()) // Lee el contenido del archivo y pasa al método que procesa el contenido
    }

    // Método para leer reglas desde un contenido JSON en formato String
    fun getRulesFromJson(jsonContent: String): List<Rule> {
        val mapper = jacksonObjectMapper()
        val formattingRules = mapper.readValue(jsonContent, FormattingRules::class.java)
        val rules = mutableListOf<Rule>()

        // Normalize and trim the identifier to handle variations like "camel case" or "snake case"
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

        return rules
    }
}
