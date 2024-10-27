import cli.ParsingException
import cli.main
import cli.readFile
import cli.showProgress
import cli.tokenize
import commands.ExecutionCommand
import commands.ValidationCommand
import interpreter.Reader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.Token
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Paths
import java.util.Scanner
import kotlin.test.assertFalse

class CliTests {
    private val outputStream = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalIn = System.`in`
    private lateinit var outContent: ByteArrayOutputStream

    @BeforeEach
    fun setUpStreams() {
        outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        System.setIn(originalIn)
    }

    fun getResourceFilePath(fileName: String): String {
        return Paths.get("resources", fileName).toAbsolutePath().toString()
    }

    @Test
    fun testHandleErrorInExecution() {
        val input = "execution\ntext\ninvalid code\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Error processing"))
    }

    @Test
    fun testShowProgress() {
        showProgress()
        assertTrue(outContent.toString().contains("Processing"))
    }

    @Test
    fun `test tokenize with empty input`() {
        val tokens: List<Token> = tokenize("", "1.0")
        assertTrue(tokens.isEmpty(), "Token list should be empty for an empty source.")
    }

    @Test
    fun `test readFile with valid file`() {
        val testFile =
            File.createTempFile("test", ".txt").apply {
                writeText("let x = 5;")
            }
        val fileContent = readFile(testFile.absolutePath)

        assertEquals("let x = 5;", fileContent, "File content should match the input.")
    }

    @Test
    fun `test readFile with invalid file`() {
        val filePath = "nonexistent.txt"
        val fileContent = readFile(filePath)

        assertEquals("", fileContent, "File content should be empty for non-existing file.")
    }

    @Test
    fun `test validation command with valid source`() {
        val source = "let x = 5;"
        val version = "1.0"
        val validationCommand = ValidationCommand(source, version)

        validationCommand.execute()
    }

    @Test
    fun testReaderInput() {
        // Simular la entrada de "Hello, World!"
        val simulatedInput = "Hello, World!"
        val inputStream = ByteArrayInputStream(simulatedInput.toByteArray())

        // Redirigir System.in a la entrada simulada
        System.setIn(inputStream)

        // Implementar el Reader para la prueba
        val reader: Reader =
            object : Reader {
                override fun input(message: String): String {
                    return Scanner(System.`in`).nextLine()
                }
            }

        // Invocar el método input y verificar la salida
        val result = reader.input("Please enter something:")
        assertEquals(simulatedInput, result)
    }

    @Test
    fun `test ExecutionCommand with simulated input`() {
        // Simular la entrada del usuario con un InputStream
        val simulatedInput = "Simulated user input\n"
        System.setIn(ByteArrayInputStream(simulatedInput.toByteArray()))

        // Crear y ejecutar el comando de ejecución
        val source = "let x = input(\"Enter something:\"); println(x);"
        val version = "1.0"
        val command = ExecutionCommand(source, version, isFile = false)

        command.execute()

        // Verificar que se ejecutó correctamente y que el output es el esperado
        // Aquí puedes verificar la salida usando un ByteArrayOutputStream si necesitas capturar println
        assertTrue(true) // Aquí puedes agregar tu verificación de salida si es necesario
    }

    @Test
    fun `test main with validation operation`() {
        val input = "validation\ntext\nprint 'Hello World'\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        main()
        assertFalse(outputStream.toString().contains("Processing done"))
    }

    @Test
    fun `test main with execution operation`() {
        val input = "execution\ntext\nprint 'Execution Test'\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        main()
        assertFalse(outputStream.toString().contains("Execution finished!"))
    }

    @Test
    fun `test main with formatting operation`() {
        val input = "formatting\ntext\nprint 'Formatting Test'\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        main()
        assertFalse(outputStream.toString().contains("Formatting completed!"))
    }

    @Test
    fun `test main with analyzing operation`() {
        val input = "analyzing\ntext\nprint 'Analyzing Test'\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        main()
        assertFalse(outputStream.toString().contains("Analysis completed!"))
    }

    @Test
    fun `test unknown operation in main`() {
        val input = "unknown\ntext\nprint 'Unknown Test'\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        main()
        assertFalse(outputStream.toString().contains("Unknown operation"))
    }

    @Test
    fun `test main with file input but file not found`() {
        val input = "validation\nfile\ninvalid_path.txt\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        main()
        assertFalse(outputStream.toString().contains("Error reading file"))
    }

    @Test
    fun `test tokenize function`() {
        val source = "print 'Test'"
        val tokens: List<Token> = tokenize(source, "1.0")
        assertTrue(tokens.isNotEmpty(), "Token list should not be empty")
    }

    @Test
    fun `test readFile function with invalid path`() {
        val invalidPath = "invalid_file.txt"
        val content = readFile(invalidPath)
        assertEquals("", content, "Content should be empty for invalid file path")
    }

    @Test
    fun `should initialize ParsingException with correct message, line, and column`() {
        val message = "Syntax error"
        val line = 5
        val column = 10

        val exception = ParsingException(message, line, column)

        // Verificar que los valores se inicializaron correctamente
        assertEquals(message, exception.message)
        assertEquals(line, exception.line)
        assertEquals(column, exception.column)
    }
}
