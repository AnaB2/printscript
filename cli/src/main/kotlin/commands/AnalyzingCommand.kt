package commands

import cli.handleError
import cli.showProgress
import cli.tokenize
import linter.Linter
import linter.LinterVersion
import parser.Parser
import token.Token
import java.io.File

class AnalyzingCommand(private val source: String, private val version: String) : Command {
    override fun execute() {
        try {
            println("Analyzing...")
            showProgress()

            // Tokenize the source
            val tokens: List<Token> = tokenize(source, version)

            // Print the tokens with their positions for debugging
            println("Tokens:")
            tokens.forEach { token ->
                println("Token: ${token.value} at position: ${token.getPosition()}")
            }

            // Parse the tokens into AST nodes
            val astNodes = Parser().execute(tokens)

            // Get the correct linter version
            val linterVersion =
                LinterVersion.values().find { it.version == version }
                    ?: throw IllegalArgumentException("Invalid linter version: $version")

            val linter = Linter(linterVersion)

            // Use an absolute path for the linter rules JSON
            val absolutePath = "C:\\Users\\vgian\\PrintScript\\cli\\src\\test\\resources\\LinterRules.json"
            val jsonString = File(absolutePath).readText()
            linter.readJson(jsonString)

            // Perform linter check
            val linterOutput = linter.check(astNodes)

            // Print original source code
            println("Original Source Code:\n$source\n")

            // Check for broken rules
            val brokenRules = linterOutput.getBrokenRules()
            if (brokenRules.isEmpty()) {
                println("No issues found. The code adheres to the rules.")
            } else {
                println("Issues found:")
                brokenRules.forEach { rule ->
                    println("Rule broken: ${rule.ruleDescription} at position: ${rule.errorPosition}")
                }
            }
            // Debug: Print the details of broken rules and their positions
            println("Detailed broken rules:")
            brokenRules.forEach { rule ->
                println("Rule: ${rule.ruleDescription}")
                println("Error Position: ${rule.errorPosition}")
            }
/*
            // Generate and save the reports
            val txtReport = linter.createTxtContent(brokenRules)
            val htmlReport = linter.createHtmlContent(brokenRules)
            linter.writeToFile(txtReport, "analysis_report.txt")
            linter.writeToFile(htmlReport, "analysis_report.html")
 */
            println("Analysis completed!")
        } catch (e: Exception) {
            handleError(e, source)
        }
    }
}
