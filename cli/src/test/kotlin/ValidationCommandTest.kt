import commands.ValidationCommand
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertTrue

class ValidationCommandTest {
    private fun getResourceFiles(): List<File> {
        val resourceDir = File("src/test/resources")
        return resourceDir.listFiles()?.filter { it.extension == "txt" } ?: emptyList()
    }

    @Test
    fun testValidationCommandWithResources() {
        val resourceFiles = getResourceFiles()
        assertTrue(resourceFiles.isNotEmpty(), "No resource files found")

        resourceFiles.forEach { file ->
            val source = file.readText()
            val version = "1.0"

            try {
                val command = ValidationCommand(source, version)
                command.execute()
                // assertEquals(expectedOutput, actualOutput)
            } catch (e: Exception) {
            }
        }
    }
}
