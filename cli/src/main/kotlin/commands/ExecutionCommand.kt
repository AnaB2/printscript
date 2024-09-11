package commands

import cli.handleError
import cli.showProgress
import cli.tokenize
import interpreter.Interpreter
import parser.Parser

class ExecutionCommand(private val source: String, private val version: String, private val isFile: Boolean) : Command {
    override fun execute() {
        try {
            println("Executing...")
            showProgress()

            val tokens = tokenize(source, version)
            val parser = Parser()
            val astNodes = parser.execute(tokens)

            val interpreter = Interpreter()
            for (node in astNodes) {
                interpreter.evaluate(node)
            }

            println("Execution finished!")
        } catch (e: Exception) {
            handleError(e, source)
        }
    }
}
