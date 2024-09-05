package commands

import Parser
import ParsingException
import tokenize

class ValidationCommand(private val source: String, private val version: String) : Command {
    override fun execute() {
        println("Validating content...")
        val tokens = tokenize(source, version)

        val parser = Parser()
        try {
            val astNodes = parser.execute(tokens)
            println("Validation successful.")
        } catch (e: Exception) {
            println("Validation failed: ${e.message}")
            if (e is ParsingException) {
                println("Error at line ${e.line}, column ${e.column}")
            }
        }
    }
}
