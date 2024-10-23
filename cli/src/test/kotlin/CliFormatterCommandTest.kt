import cli.ParsingException
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
        val resourceFiles = getResourceFiles()
        assertTrue(resourceFiles.isNotEmpty(), "No resource files found")

        resourceFiles.forEach { file ->
            val source = file.readText()
            val version = "1.0"
            val args = emptyList<String>()

            try {
                val command = FormattingCommand(source, version, args)
                command.execute()

                // val expectedFormattedOutput = File("src/test/resources/expected/${file.name}").readText()
                // assertEquals(expectedFormattedOutput, command.getFormattedOutput())
            } catch (e: Exception) {
                assertTrue(e is ParsingException, "Unexpected exception type: ${e::class.simpleName}")
                println("Failed to format ${file.name}: ${e.message}")
            }
        }
    }
}
