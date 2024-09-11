import commands.ExecutionCommand
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertTrue
import kotlin.test.fail

class ExecutionCommandTest {
    private fun getResourceFiles(): List<File> {
        val resourceDir = File("src/test/resources")
        return resourceDir.listFiles()?.filter { it.extension == "txt" } ?: emptyList()
    }

    @Test
    fun testExecutionCommandWithResources() {
        val resourceFiles = getResourceFiles()
        assertTrue(resourceFiles.isNotEmpty(), "No resource files found")

        resourceFiles.forEach { file ->
            val source = file.readText()
            val version = "1.0" // Puedes ajustar la versión según sea necesario
            val isFileSource = true // Si estás probando con archivos, este valor será `true`

            try {
                val command = ExecutionCommand(source, version, isFileSource)
                command.execute()
                // Puedes agregar aserciones adicionales basadas en el resultado esperado
                // Por ejemplo:
                // assertEquals(expectedOutput, actualOutput)
            } catch (e: Exception) {
                // Puedes agregar validaciones adicionales basadas en los errores esperados
                fail("Failed to execute ${file.name}: ${e.message}")
            }
        }
    }
}
