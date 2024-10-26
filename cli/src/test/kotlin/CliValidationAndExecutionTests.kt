import cli.main
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue

class CliValidationAndExecutionTests {
    private val originalOut = System.out
    private val originalIn = System.`in`
    private lateinit var outContent: ByteArrayOutputStream

    @BeforeEach
    fun setUpStreams() {
        outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        System.setIn(originalIn)
    }

    @Test
    fun testValidationWithArithmeticOperations() {
        val inputCode =
            """
            let x: number = 5 + 3 * (2 - 1);
            """.trimIndent()
        val input = "validation\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation successful."))
    }

    @Test
    fun testExecutionWithPrintStatements() {
        val inputCode =
            """
            println('Hello, World!');
            """.trimIndent()
        val input = "execution\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Executing..."))
        assertTrue(outContent.toString().contains("Hello, World!"))
        assertTrue(outContent.toString().contains("Execution finished!"))
    }

    @Test
    fun testValidationWithStringManipulation() {
        val inputCode =
            """
            let message: string = 'Hello' + ', ' + 'world!';
            """.trimIndent()
        val input = "validation\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation successful."))
    }

    @Test
    fun testValidationWithComplexExpression() {
        val inputCode =
            """
            let result: number = (10 + 5) * (3 - 2) / 5;
            """.trimIndent()
        val input = "validation\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation successful."))
    }
}
