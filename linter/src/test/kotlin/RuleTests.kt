import ast.Tokenizer
import org.example.lexer.Lexer
import org.example.lexer.TokenMapper
import parser.Parser
import rules.CamelCaseRule
import rules.PrintOnlyRule
import rules.SnakeCaseRule
import kotlin.test.Test
import kotlin.test.assertTrue

class RuleTests {
    private val tokenMapper = TokenMapper("1.0")
    private val lexer = Lexer(tokenMapper)
    private val parser = Parser()
    private val tokenizer = Tokenizer()

//    @Test
//    fun `test is not in camel case`() {
//        val input = "let my_variable:string = \"this is a variable\"; "
//        val tokens = lexer.execute(input)
//        val trees = parser.execute(tokens)
//        val tokensList = tokenizer.parseToTokens(trees)
//        val camelCase = CamelCaseRule()
//        val brokenRules = camelCase.applyRule(tokensList)
//        assertTrue(brokenRules.isNotEmpty())
//        assertEquals("The following identifier must be in camel case", brokenRules[0].ruleDescription)
//        assertEquals(TokenPosition(0, 4), brokenRules[0].errorPosition)
//    }

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

//    @Test
//    fun `test is in camel case with multiple variables`() {
//        val input = "let myVariable:string = \"this is a variable\"; let myVariable2:string = \"this is a variable\"; "
//        val tokens = lexer.execute(input)
//        val trees = parser.execute(tokens)
//        val tokensList = tokenizer.parseToTokens(trees)
//        val camelCase = CamelCaseRule()
//        val brokenRules = camelCase.applyRule(tokensList)
//        assertTrue(brokenRules.isEmpty())
//    }

//    @Test
//    fun `test is not in snake case`() {
//        val input = "let myVariable:string = \"this is a variable\"; "
//        val tokens = lexer.execute(input)
//        val trees = parser.execute(tokens)
//        val tokensList = tokenizer.parseToTokens(trees)
//        val snakeCase = SnakeCaseRule()
//        val brokenRules = snakeCase.applyRule(tokensList)
//        assertTrue(brokenRules.isNotEmpty())
//        assertEquals("The following identifier must be in snake case", brokenRules[0].ruleDescription)
//        assertEquals(TokenPosition(0, 4), brokenRules[0].errorPosition)
//    }

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

//    @Test
//    fun `test is in snake case with multiple variables`() {
//        val input = "let my_variable:string = \"this is a variable\"; let my_variable2:string = \"this is a variable\"; "
//        val tokens = lexer.execute(input)
//        val trees = parser.execute(tokens)
//        val tokensList = tokenizer.parseToTokens(trees)
//        val snakeCase = SnakeCaseRule()
//        val brokenRules = snakeCase.applyRule(tokensList)
//        assertTrue(brokenRules.isEmpty())
//    }

//    @Test
//    fun `test is not in print only`() {
//        val input = "println(my_variable + 5); "
//        val tokens = lexer.execute(input)
//        val trees = parser.execute(tokens)
//        val tokensList = tokenizer.parseToTokens(trees)
//        val printOnly = PrintOnlyRule()
//        val brokenRules = printOnly.applyRule(tokensList)
//        assertTrue(brokenRules.isNotEmpty())
//        assertEquals("Println must not be called with an expression", brokenRules[0].ruleDescription)
//        assertEquals(TokenPosition(0, 0), brokenRules[0].errorPosition)
//    }

    @Test
    fun `test println is called without expression`() {
        val input = "println(my_variable); "
        val tokens = lexer.execute(input)
        val trees = parser.execute(tokens)
        val tokensList = tokenizer.parseToTokens(trees)
        val printOnly = PrintOnlyRule()
        val brokenRules = printOnly.applyRule(tokensList)
        assertTrue(brokenRules.isEmpty())
    }

//    @Test
//    fun `test is not in input only`() {
//        val input = "readinput(my_variable + 5); "
//        val tokens = lexer.execute(input)
//        val trees = parser.execute(tokens)
//        val tokensList = tokenizer.parseToTokens(trees)
//        val inputOnly = InputOnlyRule()
//        val brokenRules = inputOnly.applyRule(tokensList)
//        assertTrue(brokenRules.isNotEmpty())
//        assertEquals("ReadInputs must not be called with an expression", brokenRules[0].ruleDescription)
//        assertEquals(TokenPosition(0, 0), brokenRules[0].errorPosition)
//    }

//    @Test
//    fun `test input is called without expression`() {
//        val input = "readinput(my_variable); "
//        val tokens = lexer.execute(input)
//        val trees = parser.execute(tokens)
//        val tokensList = tokenizer.parseToTokens(trees)
//        val inputOnly = InputOnlyRule()
//        val brokenRules = inputOnly.applyRule(tokensList)
//        assertTrue(brokenRules.isEmpty())
//    }
}
