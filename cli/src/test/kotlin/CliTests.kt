
import cli.main
import cli.readFile
import cli.showProgress
import cli.tokenize
import commands.ValidationCommand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import token.Token
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Paths
import kotlin.test.Test

class CliTests {
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

    /*
    @Test
    fun testFormatOperation() {
        val input = "formatting\ntext\nprint('format this text')\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Formatting completed!"))
    }

    @Test
    fun testAnalyzeOperation() {
        val input = "analyzing\ntext\nif (true) {}\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Analysis completed!"))
    }


     */
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

        // Simular la ejecución y verificar que no hay errores
        validationCommand.execute()
    }
}
