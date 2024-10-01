package commands

import cli.handleError
import cli.showProgress
import cli.tokenize
import formatter.FormatterBuilderPS
import interpreter.Printer
import parser.Parser

class FormattingCommand(private val source: String, private val version: String, private val args: List<String>) : Command {
    override fun execute() {
        try {
            println("Formatting...")
            showProgress()

            val tokens = tokenize(source, version)
            val astNodes = Parser().execute(tokens)
            val formattedCode = buildFormatter().format(astNodes)

            // Definir el Printer como en ExecutionCommand
            val printer: Printer =
                object : Printer {
                    override fun print(message: String) {
                        println(message)
                    }
                }

            // Usar el Printer para las salidas
            if (source == formattedCode) {
                printer.print("No changes made. The code is already formatted.")
            } else {
                printer.print("The following changes were made to the code:")
                printCodeChanges(source, formattedCode, printer)
            }

            printer.print("Formatting completed!")
        } catch (e: Exception) {
            handleError(e, source)
        }
    }

    private fun buildFormatter() =
        FormatterBuilderPS().build(
            "C:/Users/vgian/PrintScript/cli/src/test/resources/StandardRules.json",
            "1.1",
        )

    // Modifica esta función para usar el Printer
    private fun printCodeChanges(
        original: String,
        formatted: String,
        printer: Printer,
    ) {
        original.lines().forEachIndexed { i, originalLine ->
            formatted.lines().getOrNull(i)?.let { formattedLine ->
                if (originalLine != formattedLine) {
                    printer.print("Line ${i + 1}:\nOriginal:   $originalLine\nFormatted: $formattedLine")
                }
            } ?: printer.print("Line ${i + 1} removed: $originalLine")
        }

        formatted.lines().drop(original.lines().size).forEachIndexed { i, addedLine ->
            printer.print("Line ${original.lines().size + i + 1} added: $addedLine")
        }
    }
}
