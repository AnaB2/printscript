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
            let x: number = 5 + 3 * (2 - 1)
            """.trimIndent()
        val input = "validation\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation successful."))
    }

/*
   @Test
    fun testValidationWithArithmeticOperations3() {
        val inputCode = "let x: number = 5 + 3 * (2 - 1)\n"
        val input = "validation\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation successful."))
    }



    @Test
    fun testValidationWithTypeMismatch2() {
        val inputCode = "let x: string = 2 + \"5\"; println(x);\n"
        val input = "validation\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation failed:"))
        assertTrue(outContent.toString().contains("Type mismatch error"))
    }



    @Test
    fun testValidationWithTypeMismatch() {
        val inputCode = """let x: string = 2 + "5"; println(x);""".trimIndent()
        val input = "validation\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation failed:"))
        assertTrue(outContent.toString().contains("Type mismatch error"))
    }



    @Test
    fun testValidationWithDeclarations() {
        val inputCode =
            """
            let x: number = 10
            let y: number = x + 5
            """.trimIndent()
        val input = "validation\ntext\n$inputCode\n1.0\n" // Make sure version is at the end
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation successful."))
    }



    @Test
    fun testExecutionWithArithmeticOperations() {
        val inputCode =
            """
            let result: number = 10 * (5 + 2)
            println(result)
            """.trimIndent()
        val input = "execution\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // Redirect System.out
        val outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))

        try {
            main() // Call the main function
        } finally {
            System.setOut(System.out) // Restore System.out
        }

        // Print the output for debugging
        println(outContent.toString())

        // Assertions
        assertTrue(outContent.toString().contains("Executing..."))
        assertTrue(outContent.toString().contains("Execution finished!"))
    }


 */
    @Test
    fun testExecutionWithPrintStatements() {
        val inputCode =
            """
            println('Hello, World!')
            """.trimIndent()
        val input = "execution\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Executing..."))
        assertTrue(outContent.toString().contains("Hello, World!"))
        assertTrue(outContent.toString().contains("Execution finished!"))
    }

/*
    @Test
    fun testExecutionWithConditionalStatements() {
        val inputCode =
            """
            let x: number = 10
            if (x > 5) {
                println('Greater than 5')
            }
            """.trimIndent()
        val input = "execution\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Executing..."))
        assertTrue(outContent.toString().contains("Greater than 5"))
        assertTrue(outContent.toString().contains("Execution finished!"))
    }

    @Test
    fun testValidationWithInvalidSyntax() {
        val inputCode =
            """
            let x: number = 5 + * 2
            """.trimIndent()
        val input = "validation\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation failed:"))
    }


 */
    @Test
    fun testValidationWithStringManipulation() {
        val inputCode =
            """
            let message: string = 'Hello' + ', ' + 'world!'
            """.trimIndent()
        val input = "validation\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation successful."))
    }

/*
    @Test
    fun testExecutionWithStringManipulation() {
        val inputCode =
            """
            let greeting: string = 'Hello'
            let name: string = 'Alice'
            let message: string = greeting + ', ' + name + '!'
            println(message)
            """.trimIndent()
        val input = "execution\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Executing..."))
        assertTrue(outContent.toString().contains("Hello, Alice!"))
        assertTrue(outContent.toString().contains("Execution finished!"))
    }



    @Test
    fun testExecutionWithMultipleStatements() {
        val inputCode =
            """
            let x: number = 10
            let y: number = 20
            let z: number = x + y
            println(z)
            """.trimIndent()
        val input = "execution\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Executing..."))
        assertTrue(outContent.toString().contains("30"))
        assertTrue(outContent.toString().contains("Execution finished!"))
    }

    @Test
    fun testExecutionWithBooleanCondition() {
        val inputCode =
            """
            let isTrue: boolean = true
            if (isTrue) {
                println('This is true')
            }
            """.trimIndent()
        val input = "execution\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Executing..."))
        assertTrue(outContent.toString().contains("This is true"))
        assertTrue(outContent.toString().contains("Execution finished!"))
    }
*/
    @Test
    fun testValidationWithComplexExpression() {
        val inputCode =
            """
            let result: number = (10 + 5) * (3 - 2) / 5
            """.trimIndent()
        val input = "validation\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Validating content..."))
        assertTrue(outContent.toString().contains("Validation successful."))
    }
/*
    @Test
    fun testExecutionWithNestedConditionals() {
        val inputCode =
            """
            let x: number = 10
            if (x > 5) {
                if (x < 15) {
                    println('x is between 5 and 15')
                }
            }
            """.trimIndent()
        val input = "execution\ntext\n$inputCode\n1.0\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        main()

        assertTrue(outContent.toString().contains("Executing..."))
        assertTrue(outContent.toString().contains("x is between 5 and 15"))
        assertTrue(outContent.toString().contains("Execution finished!"))
    }

 */
}
