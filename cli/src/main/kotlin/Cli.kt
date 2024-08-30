import org.example.Lexer
import org.example.TokenMapper
import token.Token
import java.io.File
import java.io.BufferedReader
import java.io.InputStream

fun main() {
    println("Please choose the operation (validation, execution, formatting, analyzing):")
    val operation = readLine()?.lowercase()

    println("Do you want to input a file or text? (file/text):")
    val sourceType = readLine()?.lowercase()

    val source = when (sourceType) {
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

    // Perform the chosen operation
    when (operation) {
        "validation" -> validate(source, version)
        "execution" -> execute(source, version, sourceType == "file")
        "formatting" -> format(source, version, emptyList(), sourceType == "file")
        "analyzing" -> analyze(source, version, sourceType == "file")
        else -> println("Unknown operation: $operation")
    }
}

// Read file content based on the source type
fun readFile(filePath: String): String {
    return try {
        // Choose the appropriate method to read the file
        // You can switch between these methods based on your needs

        // Method 1: Using BufferedReader
        File(filePath).bufferedReader().use { it.readText() }

        // Method 2: Using InputStream (read all lines)
        // File(filePath).inputStream().bufferedReader().use { it.readText() }

        // Method 3: Using File directly (read lines)
        // File(filePath).useLines { lines -> lines.joinToString("\n") }

    } catch (e: Exception) {
        println("Error processing file: $e")
        ""
    }
}

fun validate(source: String, version: String) {
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

fun execute(source: String, version: String, isFile: Boolean) {
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

fun format(source: String, version: String, args: List<String>, isFile: Boolean) {
    try {
        println("Formatting...")
        showProgress()

        // Placeholder, implement when Formatter is available
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
        println("Analysis completed!")
    } catch (e: Exception) {
        handleError(e, source)
    }
}

fun tokenize(source: String, version: String): List<Token> {
    val tokenMapper = TokenMapper(version)
    val lexer = Lexer(tokenMapper)

    return lexer.execute(source)
}

fun showProgress() {
    print("Processing")
    for (i in 1..5) {
        Thread.sleep(500)
        print(".")
    }
    println(" done.")
}

fun handleError(e: Exception, source: String) {
    println("Error processing $source: ${e.message}")
    if (e is ParsingException) {
        println("Error at line ${e.line}, column ${e.column}")
    }
}

class ParsingException(message: String, val line: Int, val column: Int) : Exception(message)
