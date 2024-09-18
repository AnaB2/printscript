package formatter

import formatOperations.FormatAssignation
import formatOperations.FormatBinary
import formatOperations.FormatBlock
import formatOperations.FormatConditional
import formatOperations.FormatDeclaration
import formatOperations.FormatFunction
import formatOperations.FormatLiteral
import formatOperations.FormatOperation
import formatOperations.FormatPrint
import token.TokenType

class FormatterBuilderPS : FormatterBuilder {
    override fun build(
        rulesPath: String,
        version: String,
    ): Formatter {
        return when (version) {
            "1.0" -> formatter10(rulesPath)
            "1.1" -> formatter11(rulesPath)
            else -> throw IllegalArgumentException("Formatter version $version doesn't exist.")
        }
    }

    private fun formatter10(rulesPath: String): Formatter {
        val formatOperations: List<FormatOperation> =
            listOf(
                FormatAssignation(),
                FormatBinary(),
                FormatBlock(),
                FormatFunction(),
                FormatLiteral(),
                FormatPrint(),
                FormatDeclaration(
                    getAllowedDeclarationKeywords("1.0"),
                    getAllowedValueTypes("1.0"),
                ),
            )
        return FormatterPS(rulesPath, formatOperations)
    }

    private fun formatter11(rulesPath: String): Formatter {
        val formatOperations: List<FormatOperation> =
            listOf(
                FormatAssignation(),
                FormatBinary(),
                FormatBlock(),
                FormatFunction(),
                FormatLiteral(),
                FormatPrint(),
                FormatDeclaration(
                    getAllowedDeclarationKeywords("1.1"),
                    getAllowedValueTypes("1.1"),
                ),
                FormatConditional(),
            )
        return FormatterPS(rulesPath, formatOperations)
    }

    private fun getAllowedDeclarationKeywords(version: String): List<String> {
        return when (version) {
            "1.0" -> listOf("let")
            "1.1" -> listOf("let", "const")
            else -> throw IllegalArgumentException("unsupported version")
        }
    }

    private fun getAllowedValueTypes(version: String): Map<TokenType, String> {
        return when (version) {
            "1.0" ->
                mapOf(
                    TokenType.NUMBERLITERAL to "number",
                    TokenType.STRINGLITERAL to "string",
                )
            "1.1" ->
                mapOf(
                    TokenType.NUMBERLITERAL to "number",
                    TokenType.STRINGLITERAL to "string",
                    TokenType.BOOLEANLITERAL to "boolean",
                )
            else -> throw IllegalArgumentException("unsupported version")
        }
    }
}
