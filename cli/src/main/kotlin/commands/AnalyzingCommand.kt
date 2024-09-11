package commands

import Linter
import LinterVersion
import Parser
import handleError
import showProgress
import tokenize

class AnalyzingCommand(private val source: String, private val version: String) : Command {
    override fun execute() {
        try {
            println("Analyzing...")
            showProgress()

            // Tokenizar el código fuente usando el método tokenize del CLI
            val tokens = tokenize(source, version)

            // Analizar el código para generar el AST
            val astNodes = Parser().execute(tokens)

            // Convertir la versión del linter desde una cadena a un tipo de versión
            val linterVersion =
                LinterVersion.values().find { it.version == version }
                    ?: throw IllegalArgumentException("Invalid linter version: $version")

            // Crear el linter y leer las reglas desde un archivo JSON
            val linter = Linter(linterVersion)
            linter.readJson("C:\\Users\\vgian\\PrintScript\\cli\\src\\test\\resources\\LinterRules.json")

            // Ejecutar el linter en el AST
            val linterOutput = linter.check(astNodes)

            // Imprimir el código fuente original
            println("Original Source Code:\n$source")

            // Crear el informe de errores y mostrar las reglas rotas
            val brokenRules = linterOutput.getBrokenRules()
            if (brokenRules.isEmpty()) {
                println("No issues found. The code adheres to the rules.")
            } else {
                println("Issues found:")
                brokenRules.forEach { rule ->
                    println("RuleDescription: ${rule.ruleDescription}")
                    println("ErrorPosition: ${rule.errorPosition}")
                    println()
                }
            }

            // Crear los informes TXT y HTML
            val txtReport = linter.createTxtContent(brokenRules)
            val htmlReport = linter.createHtmlContent(brokenRules)

            // Escribir los informes a archivos
            linter.writeToFile(txtReport, "analysis_report.txt")
            linter.writeToFile(htmlReport, "analysis_report.html")

            println("Analysis completed!")
        } catch (e: Exception) {
            handleError(e, source)
        }
    }
}
