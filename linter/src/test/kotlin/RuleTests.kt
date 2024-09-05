import org.example.Lexer
import org.example.TokenMapper
import rules.CamelCaseRule
import rules.SnakeCaseRule
import token.TokenPosition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RuleTests {
    private val tokenMapper = TokenMapper("1.0")
    private val lexer = Lexer(tokenMapper)
    private val parser = Parser()
    private val tokenizer = Tokenizer()

    @Test
    fun `test is not in camel case`() {
        val input = "let my_variable:string = \"this is a variable\"; "
        val tokens = lexer.execute(input)
        val trees = parser.execute(tokens)
        val tokensList = tokenizer.parseToTokens(trees)
        val camelCase = CamelCaseRule()
        val brokenRules = camelCase.applyRule(tokensList)
        assertTrue(brokenRules.isNotEmpty())
        assertEquals("The following identifier must be in camel case", brokenRules[0].ruleDescription)
        assertEquals(TokenPosition(0, 4), brokenRules[0].errorPosition)
    }

    @Test
    fun `test is in camel case`() {
        val input = "let myVariable:string = \"this is a variable\"; "
        val tokens = lexer.execute(input)
        val trees = parser.execute(tokens)
        val tokensList = tokenizer.parseToTokens(trees)
        val camelCase = CamelCaseRule()
        val brokenRules = camelCase.applyRule(tokensList)
        assertTrue(brokenRules.isEmpty())
    }

    @Test
    fun `test is in camel case with multiple variables`() {
        val input = "let myVariable:string = \"this is a variable\"; let myVariable2:string = \"this is a variable\"; "
        val tokens = lexer.execute(input)
        val trees = parser.execute(tokens)
        val tokensList = tokenizer.parseToTokens(trees)
        val camelCase = CamelCaseRule()
        val brokenRules = camelCase.applyRule(tokensList)
        assertTrue(brokenRules.isEmpty())
    }

    @Test
    fun `test is not in snake case`() {
        val input = "let my_variable:string = \"this is a variable\"; "
        val tokens = lexer.execute(input)
        val trees = parser.execute(tokens)
        val tokensList = tokenizer.parseToTokens(trees)
        val snakeCase = SnakeCaseRule()
        val brokenRules = snakeCase.applyRule(tokensList)
        assertTrue(brokenRules.isNotEmpty())
        assertEquals("The following identifier must be in snake", brokenRules[0].ruleDescription)
        assertEquals(TokenPosition(0, 4), brokenRules[0].errorPosition)
    }

    @Test
    fun `test is in snake case`() {
        val input = "let my_variable:string = \"this is a variable\"; "
        val tokens = lexer.execute(input)
        val trees = parser.execute(tokens)
        val tokensList = tokenizer.parseToTokens(trees)
        val snakeCase = SnakeCaseRule()
        val brokenRules = snakeCase.applyRule(tokensList)
        assertTrue(brokenRules.isEmpty())
    }
}
