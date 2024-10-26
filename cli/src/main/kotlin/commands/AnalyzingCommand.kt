package commands

import cli.handleError
import cli.showProgress
import cli.tokenize
import linter.Linter
import linter.LinterVersion
import parser.Parser
import java.io.File

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

            // Usar un path absoluto
            val absolutePath = "C:\\Users\\vgian\\PrintScript\\cli\\src\\test\\resources\\LinterRules.json"
            val jsonString = File(absolutePath).readText()
            linter.readJson(jsonString) // Llama al método con el String

            // Obtener el resultado del linter
            val linterOutput = linter.check(astNodes)

            // Mostrar el código original
            println("Original Source Code:\n$source\n")

            // Comprobar si se aplicaron reglas y mostrar resultados
            val brokenRules = linterOutput.getBrokenRules()
            if (brokenRules.isEmpty()) {
                println("No issues found. The code adheres to the rules.")
            } else {
                println("Issues found:")
                brokenRules.forEach { rule ->
                    // Indicar que no cumple con la regla
                    println("No cumple con la regla: ${rule.ruleDescription} en la posición de error: ${rule.errorPosition}")
                }
            }

            // Generar y guardar los informes
            val txtReport = linter.createTxtContent(brokenRules)
            val htmlReport = linter.createHtmlContent(brokenRules)
            linter.writeToFile(txtReport, "analysis_report.txt")
            linter.writeToFile(htmlReport, "analysis_report.html")

            println("Analysis completed!")
        } catch (e: Exception) {
            handleError(e, source)
        }
    }
}
