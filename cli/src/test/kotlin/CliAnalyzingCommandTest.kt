import cli.ParsingException
import commands.AnalyzingCommand
import java.io.File
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
            val version = "1.0" // Ajusta la versión según sea necesario

            try {
                // Instancia del comando de análisis
                val command = AnalyzingCommand(source, version)
                command.execute()

                // Aquí puedes agregar aserciones para verificar el resultado analizado
                // Por ejemplo, si tienes un resultado esperado guardado, puedes verificarlo:
                // val expectedAnalyzedOutput = File("src/test/resources/expected/${file.name}").readText()
                // assertEquals(expectedAnalyzedOutput, command.getAnalyzedOutput())
            } catch (e: Exception) {
                // Puedes agregar validaciones si esperas ciertos errores, como ParsingException
                assertTrue(e is ParsingException, "Unexpected exception type: ${e::class.simpleName}")
                println("Failed to analyze ${file.name}: ${e.message}")
            }
        }
    }
}
