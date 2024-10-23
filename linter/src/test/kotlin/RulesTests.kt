import ast.Tokenizer
import lexer.Lexer
import lexer.TokenMapper
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.Parser
import rules.CamelCaseRule
import rules.PrintOnlyRule
import rules.SnakeCaseRule

class RulesTests {
    private val tokenMapper = TokenMapper("1.1")
    private val lexer = Lexer(tokenMapper)
    private val parser = Parser()
    private val tokenizer = Tokenizer()

    @Test
    fun `test identifier is in camel case`() {
        val input = "let myVariable:string = \"this is a variable\";"
        val tokens = lexer.execute(input)
        val trees = parser.execute(tokens)
        val tokensList = tokenizer.parseToTokens(trees)

        val camelCaseRule = CamelCaseRule()
        val brokenRules = camelCaseRule.applyRule(tokensList)

        assertTrue(brokenRules.isEmpty())
    }

    @Test
    fun `test identifier is in snake case`() {
        val input = "let my_variable:string = \"this is a variable\";"
        val tokens = lexer.execute(input)
        val trees = parser.execute(tokens)
        val tokensList = tokenizer.parseToTokens(trees)

        val snakeCaseRule = SnakeCaseRule()
        val brokenRules = snakeCaseRule.applyRule(tokensList)

        assertTrue(brokenRules.isEmpty())
    }

    @Test
    fun `test println called without expression passes PrintOnlyRule`() {
        val input = "println(my_variable);"
        val tokens = lexer.execute(input)
        val trees = parser.execute(tokens)
        val tokensList = tokenizer.parseToTokens(trees)

        val printOnlyRule = PrintOnlyRule()
        val brokenRules = printOnlyRule.applyRule(tokensList)

        assertTrue(brokenRules.isEmpty())
    }
}
