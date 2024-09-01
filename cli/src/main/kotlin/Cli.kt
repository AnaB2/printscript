import org.example.Lexer
import org.example.TokenMapper
import token.Token
import java.io.File

fun main() {
    println("Please choose the operation (validation, execution, formatting, analyzing):")
    val operation = readLine()?.lowercase()

    println("Do you want to input a file or text? (file/text):")
    val sourceType = readLine()?.lowercase()

    val source = when (sourceType) {
        "file" -> {
            println("Please enter the file path:")
            readLine()
        }
        "text" -> {
            println("Please enter the text:")
            readLine()
        }
        else -> {
            println("Unknown source type: $sourceType")
            return
        }
    }

    if (source.isNullOrEmpty() || operation.isNullOrEmpty()) {
        println("Invalid input provided.")
        return
    }

    println("Please enter the version (default is 1.0):")
    val version = readLine() ?: "1.0"

    val isFile = sourceType == "file"

    when (operation) {
        "validation" -> validate(source, version, isFile)
        "execution" -> execute(source, version, isFile)
        "formatting" -> format(source, version, emptyList(), isFile)
        "analyzing" -> analyze(source, version, isFile)
        else -> println("Unknown operation: $operation")
    }
}
// Función para validar el contenido del archivo o texto
fun validate(source: String, version: String, isFile: Boolean = true) {
    // Leer el contenido del archivo o texto
    val content = if (isFile) {
        val file = File(source)
        file.readText()
    } else {
        source
    }

    println("Validating content...")

    // Tokenizar el contenido
    val tokens = tokenize(content, version, isFile)

    // Analizar los tokens
    val parser = Parser()
    try {
        val astNodes = parser.execute(tokens)
        println("Validation successful.")
    } catch (e: Exception) {
        println("Validation failed: ${e.message}")
        // Mostrar detalles adicionales si están disponibles
        if (e is ParsingException) {
            println("Error at line ${e.line}, column ${e.column}")
        }
    }
}


fun execute(source: String, version: String, isFile: Boolean) {
    try {
        println("Executing...")
        showProgress()

        val tokens = tokenize(source, version, isFile)
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

fun format(source: String, version: String, args: List<String>, isFile: Boolean) {
    try {
        println("Formatting...")
        showProgress()

        // Placeholder, implement when Formatter is available
        // val formatter = Formatter(args)
        // val formattedContent = formatter.format(source)

        println("Formatting completed!")
    } catch (e: Exception) {
        handleError(e, source)
    }
}

fun analyze(source: String, version: String, isFile: Boolean) {
    try {
        println("Analyzing...")
        showProgress()

        // Placeholder, implement when Analyzer is available
        // val analyzer = Analyzer(version)
        // val analysisReport = analyzer.analyze(source)

        println("Analysis completed!")
    } catch (e: Exception) {
        handleError(e, source)
    }
}

// Tokenize the source file or text
fun tokenize(source: String, version: String, isFile: Boolean = true): List<Token> {
    val content = if (isFile) {
        val file = File(source)
        file.readText()
    } else {
        source
    }

    val tokenMapper = TokenMapper(version)
    val lexer = Lexer(tokenMapper)

    return lexer.execute(content)
}

// Show progress while processing
fun showProgress() {
    print("Processing")
    for (i in 1..5) {
        Thread.sleep(500)
        print(".")
    }
    println(" done.")
}

// Handle and display errors with additional details if available
fun handleError(e: Exception, source: String) {
    println("Error processing $source: ${e.message}")
    // If more error details are available (like line/column), they can be printed here
    if (e is ParsingException) {
        println("Error at line ${e.line}, column ${e.column}")
    }
}

// Example exception class with line and column information
class ParsingException(message: String, val line: Int, val column: Int) : Exception(message)
