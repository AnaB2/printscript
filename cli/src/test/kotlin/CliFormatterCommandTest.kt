import commands.FormattingCommand
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class CliFormatterCommandTest {
    private fun getResourceFiles(): List<File> {
        val resourceDir = File("src/test/resources")
        return resourceDir.listFiles()?.filter { it.extension == "txt" } ?: emptyList()
    }

    @Test
    fun testFormattingCommandWithResources() {
        val resourceFiles = getResourceFiles() // Utilizas el método sin modificaciones
        assertTrue(resourceFiles.isNotEmpty(), "No resource files found")

        resourceFiles.forEach { file ->
            val source = file.readText()
            val version = "1.0" // Ajusta la versión según sea necesario
            val args = emptyList<String>() // Puedes ajustar los argumentos si tu comando lo requiere

            try {
                // Instancia del comando de formateo
                val command = FormattingCommand(source, version, args)
                command.execute()

                // Aquí puedes agregar aserciones para verificar el resultado formateado
                // Por ejemplo, si tienes un resultado esperado guardado, puedes verificarlo:
                // val expectedFormattedOutput = File("src/test/resources/expected/${file.name}").readText()
                // assertEquals(expectedFormattedOutput, command.getFormattedOutput())
            } catch (e: Exception) {
                // Puedes agregar validaciones si esperas ciertos errores, como ParsingException
                assertTrue(e is ParsingException, "Unexpected exception type: ${e::class.simpleName}")
                println("Failed to format ${file.name}: ${e.message}")
            }
        }
    }
}
