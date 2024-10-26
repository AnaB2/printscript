import linter.Linter
import linter.LinterVersion
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.assertEquals

class LinterTests {
    @Test
    fun `test read JSON from file and load rules`() {
        val linter = Linter(LinterVersion.VERSION_1_1)
        val filePath = "src/test/resources/linter_rules.json"

        linter.readJson(File(filePath).readText())

        val loadedRules = linter.getRules()
        assertEquals(3, loadedRules.size)
    }

    @Test
    fun `test error when wrong version`() {
        val linter = Linter(LinterVersion.VERSION_1_0)
        val filePath = "src/test/resources/linter_rules.json"

        assertThrows<IllegalArgumentException> {
            linter.readJson(File(filePath).readText())
        }
    }
}
