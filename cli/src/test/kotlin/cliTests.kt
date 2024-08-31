import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertTrue

class CliTests {

    @Test
    fun `test cli validation`() {
        // Arrange
        val input = "let x = 5; print(x);"
        val version = "1.0"

        // Act
        val result = runCatching {
            validate(input, version, isFile = false)
        }

        // Assert
        assertTrue(result.isSuccess, "Validation should succeed without exceptions.")
    }

    @Test
    fun `test cli execution`() {
        // Arrange
        val input = "let x = 5; print(x);"
        val version = "1.0"
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))  // Capturar la salida del sistema

        // Act
        val result = runCatching {
            // Tokenizar el input
            val tokens = tokenize(input, version, isFile = false)

            // Parsear los tokens a nodos AST
            val parser = Parser()
            val astNodes = parser.execute(tokens)

            // Evaluar los nodos AST utilizando el Interpreter
            val interpreter = Interpreter()
            for (node in astNodes) {
                interpreter.evaluate(node)
            }
        }
        // Assert
        val output = outputStream.toString().trim()
        assertTrue(result.isSuccess, "Execution should succeed without exceptions.")
        assertTrue(output.contains("5"), "Execution should print the value 5.")
    }

    // Implementar los métodos test para formatting y analyzing cuando estén disponibles
    @Test
    fun `test cli formatting`() {
        // Implementar prueba para el formateo
    }

    @Test
    fun `test cli analyzing`() {
        // Implementar prueba para el análisis
    }
}
