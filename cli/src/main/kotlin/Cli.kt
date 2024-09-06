import commands.AnalyzingCommand
import commands.ExecutionCommand
import commands.FormattingCommand
import commands.ValidationCommand
import org.example.Lexer
import org.example.TokenMapper
import token.Token
import java.io.File

fun main() {
    val invoker = CLIInvoker()

    println("Please choose the operation (validation, execution, formatting, analyzing):")
    val operation = readLine()?.lowercase()

    println("Do you want to input a file or text? (file/text):")
    val sourceType = readLine()?.lowercase()

    val source =
        when (sourceType) {
            "file" -> {
                println("Enter the file path:")
                val filePath = readLine()
                if (filePath.isNullOrEmpty()) {
                    println("File path is empty")
                    return
                }
                try {
                    val absolutePath = File(filePath).absolutePath
                    println("Attempting to read file: $absolutePath")
                    val fileContent = readFile(filePath)
                    println("File read successfully")
                    fileContent
                } catch (e: Exception) {
                    println("Error reading file: $e")
                    println("Current working directory: ${System.getProperty("user.dir")}")
                    ""
                }
            }
            "text" -> {
                println("Please enter the text:")
                readLine() ?: ""
            }
            else -> {
                println("Unknown source type: $sourceType")
                return
            }
        }

    if (source.isEmpty() || operation.isNullOrEmpty()) {
        println("Invalid input provided.")
        return
    }

    println("Please enter the version (default is 1.0):")
    val version = readLine() ?: "1.0"

    // Creación del comando basado en la operación
    val command =
        when (operation) {
            "validation" -> ValidationCommand(source, version)
            "execution" -> ExecutionCommand(source, version, sourceType == "file")
            "formatting" -> FormattingCommand(source, version, emptyList())
            "analyzing" -> AnalyzingCommand(source, version)
            else -> {
                println("Unknown operation: $operation")
                return
            }
        }

    // Ejecutar el comando seleccionado
    invoker.runCommand(command)
}

fun tokenize(
    source: String,
    version: String,
): List<Token> {
    val tokenMapper = TokenMapper(version)
    val lexer = Lexer(tokenMapper)
    return lexer.execute(source)
}

fun readFile(filePath: String): String {
    return try {
        File(filePath).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        println("Error processing file: $e")
        ""
    }
}

fun showProgress() {
    print("Processing")
    for (i in 1..5) {
        Thread.sleep(500)
        print(".")
    }
    println(" done.")
}

fun handleError(
    e: Exception,
    source: String,
) {
    println("Error processing $source: ${e.message}")
    if (e is ParsingException) {
        println("Error at line ${e.line}, column ${e.column}")
    }
}

class ParsingException(message: String, val line: Int, val column: Int) : Exception(message)
