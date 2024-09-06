import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
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
}
