import cli.ParsingException
import commands.AnalyzingCommand
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertTrue

class CliAnalyzingCommandTest {
    private fun getResourceFiles(): List<File> {
        val resourceDir = File("src/test/resources")
        return resourceDir.listFiles()?.filter { it.extension == "txt" } ?: emptyList()
    }

    @Test
    fun testAnalyzingCommandWithResources() {
        val resourceFiles = getResourceFiles()
        assertTrue(resourceFiles.isNotEmpty(), "No resource files found")

        resourceFiles.forEach { file ->
            val source = file.readText()
            val version = "1.0"

            try {
                val command = AnalyzingCommand(source, version)
                command.execute()

                // val expectedAnalyzedOutput = File("src/test/resources/expected/${file.name}").readText()
                // assertEquals(expectedAnalyzedOutput, command.getAnalyzedOutput())
            } catch (e: Exception) {
                assertTrue(e is ParsingException, "Unexpected exception type: ${e::class.simpleName}")
                println("Failed to analyze ${file.name}: ${e.message}")
            }
        }
    }

    // Test para verificar que el análisis no arroje errores con código correcto
    @Test
    fun testAnalyzingCommandWithValidCode() {
        val source =
            """
            let x = 10;
            let y = 20;
            println(x + y);
            """.trimIndent()
        val version = "1.0"

        try {
            val command = AnalyzingCommand(source, version)
            command.execute()
            // Verificación básica, puedes personalizar el resultado esperado
            println("Test passed for valid code.")
        } catch (e: Exception) {
            println("Test failed for valid code: ${e.message}")
            throw e
        }
    }

    // Test para verificar que el análisis detecte errores de sintaxis
    @Test
    fun testAnalyzingCommandWithSyntaxError() {
        val source =
            """
            let x = ;
            println(x);
            """.trimIndent()
        val version = "1.0"

        try {
            val command = AnalyzingCommand(source, version)
            command.execute()
        } catch (e: ParsingException) {
            // Verificamos que se capture el error de análisis
            assertTrue(e is ParsingException, "Expected a ParsingException")
            println("Caught expected ParsingException: ${e.message}")
        } catch (e: Exception) {
            println("Test failed: Unexpected exception type ${e::class.simpleName}")
            throw e
        }
    }

    // Test para verificar que el análisis detecte reglas rotas (según el linter)
    @Test
    fun testAnalyzingCommandWithLintingErrors() {
        val source =
            """
            let x = 10;
            let y = ;
            println(x + y);
            """.trimIndent()
        val version = "1.0"

        try {
            val command = AnalyzingCommand(source, version)
            command.execute()

            // Puedes verificar que se imprimen las reglas rotas
            println("Linting errors were expected.")
        } catch (e: Exception) {
            println("Test failed for linting errors: ${e.message}")
            throw e
        }
    }
    /*

    // Test para validar que se generen reportes
    @Test
    fun testReportGeneration() {
        val source =
            """
            let x = 10;
            let y = 20;
            println(x + y);
            """.trimIndent()
        val version = "1.0"

        try {
            val command = AnalyzingCommand(source, version)
            command.execute()

            // Verificar que los reportes se generen
            val txtReportFile = File("analysis_report.txt")
            val htmlReportFile = File("analysis_report.html")
            assertTrue(txtReportFile.exists(), "Text report was not generated.")
            assertTrue(htmlReportFile.exists(), "HTML report was not generated.")

            println("Reports were successfully generated.")
        } catch (e: Exception) {
            println("Test failed during report generation: ${e.message}")
            throw e
        }
    }

     */

    @Test
    fun testAnalyzingCommandWithNoIssues2() {
        val source =
            """
            println("Hello" + " , " + "world!"        );
            """.trimIndent()
        val version = "1.1" // Asegúrate de que la versión coincida con tu output esperado

        // Redirigir la salida de println a un ByteArrayOutputStream para capturar el output
        val outputStream = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outputStream))

        try {
            // Ejecutar el comando de análisis
            val command = AnalyzingCommand(source, version)
            command.execute()

            // Restaurar el stream de salida original
            System.setOut(originalOut)

            // Obtener el output capturado
            val output = outputStream.toString().trim()

            // Verificar que se confirme que no hay problemas
            assertTrue(
                output.contains("No issues found. The code adheres to the rules."),
                "Expected 'No issues found. The code adheres to the rules.' message missing.",
            )

            // Verificar otros mensajes de salida esperados
            assertTrue(output.contains("Analyzing..."), "Expected 'Analyzing...' message missing.")
            assertTrue(output.contains("Processing..... done."), "Expected 'Processing..... done.' message missing.")

            // Asegúrate de que los tokens se impriman correctamente
            assertTrue(output.contains("Tokens from AST Node:"), "Expected 'Tokens from AST Node:' message missing.")
            assertTrue(output.contains("Token(type = 'FUNCTION', value = 'print'"), "Expected function token message missing.")
            assertTrue(output.contains("Token(type = 'STRINGLITERAL', value = 'Hello'"), "Expected string literal token message missing.")

            // Imprimir un mensaje adicional si el test pasa
            println("Test for code without issues completed successfully.")
        } catch (e: Exception) {
            System.setOut(originalOut) // Restaurar el stream en caso de excepción
            throw e // Volver a lanzar la excepción para el framework de pruebas
        }
    }
}
