package commands

import handleError
import showProgress

class FormattingCommand(private val source: String, private val version: String, private val args: List<String>) : Command {
    override fun execute() {
        try {
            println("Formatting...")
            showProgress()
            // Implementación del formateo (placeholder)
            println("Formatting completed!")
        } catch (e: Exception) {
            handleError(e, source)
        }
    }
}
