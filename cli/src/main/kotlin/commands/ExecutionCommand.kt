package commands

import cli.handleError
import cli.showProgress
import cli.tokenize
import interpreter.Interpreter
import interpreter.Printer
import interpreter.Reader
import parser.Parser
import java.util.Scanner

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
            val reader: Reader =
                object : Reader {
                    override fun input(message: String): String {
                        val scanner = Scanner(System.`in`)
                        return scanner.nextLine()
                    }
                }
            val interpreter = Interpreter(printer, reader)
            for (node in astNodes) {
                interpreter.execute(node)
            }

            println("Execution finished!")
        } catch (e: Exception) {
            handleError(e, source)
        }
    }
}
