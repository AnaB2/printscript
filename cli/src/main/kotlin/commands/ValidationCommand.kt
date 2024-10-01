package commands

import cli.ParsingException
import cli.tokenize
import interpreter.Printer
import parser.Parser

class ValidationCommand(private val source: String, private val version: String) : Command {
    override fun execute() {
        println("Validating content...")

        // Definir el Printer como en ExecutionCommand
        val printer: Printer =
            object : Printer {
                override fun print(message: String) {
                    println(message)
                }
            }

        val tokens = tokenize(source, version)

        val parser = Parser()
        try {
            val astNodes = parser.execute(tokens)
            printer.print("Validation successful.")

            // Ejemplo de uso de astNodes: imprimir el AST para depuración
            printer.print("AST Nodes:")
            astNodes.forEach { node ->
                printer.print(node.toString()) // Asume que el método toString() está bien implementado en ASTNode
            }
        } catch (e: Exception) {
            printer.print("Validation failed: ${e.message}")
            if (e is ParsingException) {
                printer.print("Error at line ${e.line}, column ${e.column}")
            }
        }
    }
}
