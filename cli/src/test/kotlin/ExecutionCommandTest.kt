import commands.ExecutionCommand
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.assertTrue

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
            val version = "1.0"
            val isFileSource = true

            try {
                val command = ExecutionCommand(source, version, isFileSource)
                command.execute()
                // assertEquals(expectedOutput, actualOutput)
            } catch (e: Exception) {
            }
        }
    }

    @Test
    fun `test successful execution`() {
        // Simular el código fuente que se va a ejecutar
        val source = "let x : number = 10; println(x);"
        val version = "1.0"

        // Redirigir la salida de consola para capturarla
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // Simular la entrada del usuario
        val inputStream = ByteArrayInputStream("user input\n".toByteArray())
        System.setIn(inputStream)

        // Crear y ejecutar el comando
        val command = ExecutionCommand(source, version, isFile = false)
        command.execute()

        // Verificar que se ejecutó correctamente
        val output = outputStream.toString()
        assertTrue(output.contains("Executing..."))
        assertTrue(output.contains("Execution finished!"))
    }

    @Test
    fun `test execution with error`() {
        // Código con un error para forzar una excepción
        val erroneousSource = "let x : number = ;" // Sintaxis incorrecta
        val version = "1.0"

        // Redirigir la salida de consola para capturarla
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // Crear y ejecutar el comando con código erróneo
        val command = ExecutionCommand(erroneousSource, version, isFile = false)
        command.execute()

        // Verificar que se manejó el error
        val output = outputStream.toString()
        assertTrue(output.contains("Error"))
    }
}
