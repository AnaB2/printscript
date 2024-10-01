package commands

import cli.handleError
import cli.showProgress
import cli.tokenize
import interpreter.Interpreter
import interpreter.Printer
import parser.Parser

class ExecutionCommand(private val source: String, private val version: String, private val isFile: Boolean) : Command {
    override fun execute() {
        try {
            println("Executing...")
            showProgress()

            val tokens = tokenize(source, version)
            val parser = Parser()
            val astNodes = parser.execute(tokens)
            val printer: Printer =
                object : Printer {
                    override fun print(message: String) {
                        println(message)
                    }
                }
            val interpreter = Interpreter(printer)
            for (node in astNodes) {
                interpreter.execute(node)
            }

            println("Execution finished!")
        } catch (e: Exception) {
            handleError(e, source)
        }
    }
}
