package commands

import cli.handleError
import cli.showProgress
import cli.tokenize
import interpreter.Printer
import linter.Linter
import linter.LinterVersion
import parser.Parser

class AnalyzingCommand(private val source: String, private val version: String) : Command {
    override fun execute() {
        try {
            println("Analyzing...")
            showProgress()

            val tokens = tokenize(source, version)
            val astNodes = Parser().execute(tokens)

            val linterVersion =
                LinterVersion.values().find { it.version == version }
                    ?: throw IllegalArgumentException("Invalid linter version: $version")

            val linter = Linter(linterVersion)
            linter.readJson("C:\\Users\\vgian\\PrintScript\\cli\\src\\test\\resources\\LinterRules.json")

            val linterOutput = linter.check(astNodes)

            // Definir el Printer como en ExecutionCommand
            val printer: Printer =
                object : Printer {
                    override fun print(message: String) {
                        println(message)
                    }
                }

            // Usar el Printer para las salidas
            printer.print("Original Source Code:\n$source")

            val brokenRules = linterOutput.getBrokenRules()
            if (brokenRules.isEmpty()) {
                printer.print("No issues found. The code adheres to the rules.")
            } else {
                printer.print("Issues found:")
                brokenRules.forEach { rule ->
                    printer.print("RuleDescription: ${rule.ruleDescription}")
                    printer.print("ErrorPosition: ${rule.errorPosition}")
                    printer.print("")
                }
            }

            val txtReport = linter.createTxtContent(brokenRules)
            val htmlReport = linter.createHtmlContent(brokenRules)
            linter.writeToFile(txtReport, "analysis_report.txt")
            linter.writeToFile(htmlReport, "analysis_report.html")

            printer.print("Analysis completed!")
        } catch (e: Exception) {
            handleError(e, source)
        }
    }
}
