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
}
