import ast.LiteralNode
import ast.PrintNode
import linter.Linter
import linter.LinterVersion
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import token.TokenPosition
import token.TokenType


class LinterTests {

//    @Test
//    fun `test read json successfully load rules`() {
//
//        val linter = Linter(LinterVersion.VERSION_1_1)
//        linter.readJson("src/test/resources/linter_rules.json")
//        val rules = linter.getRules()
//        assertEquals(3, rules.size)
//        assertEquals("CamelCase", rules[0].getRuleName())
//        assertEquals("PrintOnly", rules[1].getRuleName())
//        assertEquals("InputOnly", rules[2].getRuleName())
//    }
//
//    @Test
//    fun `test apply rules to ast node catches broken rules`() {
//        val linter = Linter(LinterVersion.VERSION_1_1)
//        linter.readJson("src/test/resources/linter_rules.json")
//        val literalNode = LiteralNode("HelloWorld", TokenType.LITERAL, TokenPosition(1, 1))
//        val printNode = PrintNode(literalNode, TokenPosition(1, 1))
//        val trees = listOf(printNode)
//        val output = linter.check(trees)
//        assertFalse(output.isCorrect)
//    }
//
//    @Test
//    fun `test linter output broken rules correctly`() {
//        val linter = Linter(LinterVersion.VERSION_1_1)
//        linter.readJson("src/test/resources/linter_rules.json")
//        val literalNode = LiteralNode("\"Hello\" + \"world!\"", TokenType.LITERAL, TokenPosition(1, 1))
//        val printNode = PrintNode(literalNode, TokenPosition(1, 1))
//        val trees = listOf(printNode)
//        val output = linter.check(trees)
//        assertFalse(output.isCorrect)
//        assertTrue(output.getBrokenRules().isNotEmpty())
//        assertEquals("Println must not be called with an expression at 1:1", output.getBrokenRules()[0].ruleDescription)
//    }
//
//    @Test
//    fun `test throw error when linter version is incompatible`() {
//        val linter = Linter(LinterVersion.VERSION_1_0)
//        assertThrows<IllegalArgumentException> {
//            linter.readJson("src/test/resources/linter_rules.json")
//        }
//    }
}