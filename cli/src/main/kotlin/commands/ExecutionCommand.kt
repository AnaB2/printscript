package commands

import Interpreter
import Parser
import handleError
import showProgress
import tokenize

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
