import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import rules.*
import java.io.File

data class FormattingRules(
    val identifier: String,
    val enablePrintRule: Boolean,
    val enableInputRule: Boolean,
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

        if (formattingRules.enablePrintRule) {
            rules.add(PrintOnlyRule())
        }
        if (formattingRules.enableInputRule) {
            rules.add(InputOnlyRule())
        }
        return rules
    }
}