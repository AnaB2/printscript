package linter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import rules.CamelCaseRule
import rules.InputOnlyRule
import rules.PrintOnlyRule
import rules.Rule
import rules.SnakeCaseRule
import java.io.File

data class FormattingRules(
    val identifier: String,
    val enablePrintOnly: Boolean,
    val enableInputOnly: Boolean,
)

class RuleJsonReader {
    fun getRulesFromJson(path: String): List<Rule> {
        val mapper = jacksonObjectMapper()
        val file = File(path)
        val formattingRules = mapper.readValue(file, FormattingRules::class.java)
        val rules = mutableListOf<Rule>()

        when (formattingRules.identifier.lowercase()) {
            "camelcase" -> rules.add(CamelCaseRule())
            "snakecase" -> rules.add(SnakeCaseRule())
        }

        if (formattingRules.enablePrintOnly) {
            rules.add(PrintOnlyRule())
        }
        if (formattingRules.enableInputOnly) {
            rules.add(InputOnlyRule())
        }
        return rules
    }
}
