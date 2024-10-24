package formatter

import formatOperations.FormattingAssignation
import formatOperations.FormattingBinary
import formatOperations.FormattingBlock
import formatOperations.FormattingConditional
import formatOperations.FormattingDeclaration
import formatOperations.FormattingLiteral
import formatOperations.FormattingOperation
import formatOperations.FormattingPrint
import lexer.Lexer
import lexer.TokenMapper
import parser.Parser
import rules.RulesReader

open class FormatterBuilderPS : FormatterBuilder {
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
        val formattingOperations: List<FormattingOperation> =
            listOf(
                FormattingAssignation(),
                FormattingBinary(),
                FormattingBlock(),
                FormattingLiteral(),
                FormattingPrint(),
                FormattingDeclaration(
                    getAllowedDeclarationKeywords("1.0"),
                    getAllowedDataTypes("1.0"),
                ),
            )
        val rulesReader =
            RulesReader(
                mapOf(
                    "spaceBeforeColon" to Boolean::class,
                    "spaceAfterColon" to Boolean::class,
                    "spaceAroundEquals" to Boolean::class,
                    "lineBreakPrintln" to Int::class,
                ),
            )
        val lexer = Lexer(TokenMapper("1.0"))
        val parser = Parser()
        return FormatterPS(rulesReader, rulesPath, formattingOperations, lexer, parser)
    }

    private fun formatter11(rulesPath: String): Formatter {
        val formattingOperations: List<FormattingOperation> =
            listOf(
                FormattingAssignation(),
                FormattingBinary(),
                FormattingBlock(),
                FormattingLiteral(),
                FormattingPrint(),
                FormattingDeclaration(
                    getAllowedDeclarationKeywords("1.1"),
                    getAllowedDataTypes("1.1"),
                ),
                FormattingConditional(),
            )
        val rulesReader =
            RulesReader(
                mapOf(
                    "spaceBeforeColon" to Boolean::class,
                    "spaceAfterColon" to Boolean::class,
                    "spaceAroundEquals" to Boolean::class,
                    "lineBreakPrintln" to Int::class,
                    "conditionalIndentation" to Int::class,
                ),
            )
        val lexer = Lexer(TokenMapper("1.1"))
        val parser = Parser()
        return FormatterPS(rulesReader, rulesPath, formattingOperations, lexer, parser)
    }

    private fun getAllowedDeclarationKeywords(version: String): List<String> {
        return when (version) {
            "1.0" -> listOf("let")
            "1.1" -> listOf("let", "const")
            else -> throw IllegalArgumentException("unsupported version")
        }
    }

    private fun getAllowedDataTypes(version: String): List<String> {
        return when (version) {
            "1.0" ->
                listOf("number", "string")
            "1.1" ->
                listOf("number", "string", "boolean")
            else -> throw IllegalArgumentException("unsupported version")
        }
    }
}
