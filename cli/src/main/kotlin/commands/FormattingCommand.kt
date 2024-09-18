package commands

import cli.handleError
import cli.showProgress
import cli.tokenize
import formatter.FormatterBuilderPS
import parser.Parser

class FormattingCommand(private val source: String, private val version: String, private val args: List<String>) : Command {
    override fun execute() {
        try {
            println("Formatting...")
            showProgress()

            // Tokenize the source using the reusable tokenize function
            val tokens = tokenize(source, version)

            // Generate AST and format the code
            val astNodes = Parser().execute(tokens)
            val formattedCode = buildFormatter().format(astNodes)

            // Show changes if any
            if (source == formattedCode) {
                println("No changes made. The code is already formatted.")
            } else {
                println("The following changes were made to the code:")
                printCodeChanges(source, formattedCode)
            }

            println("Formatting completed!")
        } catch (e: Exception) {
            handleError(e, source)
        }
    }

    // Build the formatter
    private fun buildFormatter() =
        FormatterBuilderPS().build(
            "C:/Users/vgian/PrintScript/cli/src/test/resources/StandardRules.json",
            "1.1",
        )

    // Print the changes between original and formatted code
    private fun printCodeChanges(
        original: String,
        formatted: String,
    ) {
        original.lines().forEachIndexed { i, originalLine ->
            formatted.lines().getOrNull(i)?.let { formattedLine ->
                if (originalLine != formattedLine) {
                    println("Line ${i + 1}:\nOriginal:   $originalLine\nFormatted: $formattedLine")
                }
            } ?: println("Line ${i + 1} removed: $originalLine")
        }

        formatted.lines().drop(original.lines().size).forEachIndexed { i, addedLine ->
            println("Line ${original.lines().size + i + 1} added: $addedLine")
        }
    }
}
