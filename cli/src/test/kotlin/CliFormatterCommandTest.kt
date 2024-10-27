import cli.ParsingException
import commands.FormattingCommand
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CliFormatterCommandTest {
    private fun getResourceFiles(): List<File> {
        val resourceDir = File("src/test/resources")
        return resourceDir.listFiles()?.filter { it.extension == "txt" } ?: emptyList()
    }

    @Test
    fun testFormattingCommandWithResources() {
        val resourceFiles = getResourceFiles()
        assertTrue(resourceFiles.isNotEmpty(), "No resource files found")

        resourceFiles.forEach { file ->
            val source = file.readText()
            val version = "1.0"
            val args = emptyList<String>()

            // Redirigir la salida de println a un ByteArrayOutputStream
            val outputStream = ByteArrayOutputStream()
            val originalOut = System.out
            System.setOut(PrintStream(outputStream))

            try {
                // Ejecutar el comando de formato
                val command = FormattingCommand(source, version, args)
                command.execute()

                // Restaurar el stream de salida original
                System.setOut(originalOut)

                // Leer el output esperado si tienes un archivo esperado
                val expectedOutputFile = File("src/test/resources/expected/${file.name}")
                if (expectedOutputFile.exists()) {
                    val expectedOutput = expectedOutputFile.readText().trim()
                    assertEquals(expectedOutput, outputStream.toString().trim(), "Output mismatch for ${file.name}")
                } else {
                    println("No expected output file for ${file.name}")
                }
            } catch (e: Exception) {
                System.setOut(originalOut) // Restaurar el stream en caso de excepción
                assertTrue(e is ParsingException, "Unexpected exception type: ${e::class.simpleName}")
                println("Failed to format ${file.name}: ${e.message}")
            }
        }
    }
}
