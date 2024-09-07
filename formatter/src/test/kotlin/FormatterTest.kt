import ast.ASTNode
import formatOperations.FormatAssignation
import formatOperations.FormatBinary
import formatOperations.FormatBlock
import formatOperations.FormatConditional
import formatOperations.FormatDeclaration
import formatOperations.FormatFunction
import formatOperations.FormatLiteral
import formatOperations.FormatPrint
import org.example.Lexer
import org.example.TokenMapper
import org.junit.jupiter.api.Assertions.assertEquals
import token.Token
import kotlin.test.Test

class FormatterTest {
    private val yamlPath = "src/test/resources/rules.yaml"
    private val lexer = Lexer(TokenMapper("1.0"))
    private val parser = Parser()

    private val formatter =
        FormatterBuilderPS().build(
            yamlPath,
            listOf(
                FormatAssignation(),
                FormatBinary(),
                FormatBlock(),
                FormatConditional(),
                FormatDeclaration(),
                FormatFunction(),
                FormatLiteral(),
                FormatPrint(),
            ),
        )

    // DECLARACIÓN

    @Test
    fun `test formatter with simple numeric declaration expression`() {
        val input = "let x:number=2"
        val tokens: List<Token> = lexer.execute(input)
        val ast: List<ASTNode> = parser.execute(tokens)
        val formatted = formatter.format(ast)
        assertEquals("let x : number = 2;", formatted)
    }

    @Test
    fun `test formatter with simple text declaration expression`() {
        val input = "let x:string=\"hola\""
        val tokens: List<Token> = lexer.execute(input)
        val ast: List<ASTNode> = parser.execute(tokens)
        val formatted = formatter.format(ast)
        assertEquals("let x : string = \"hola\";", formatted)
    }

    // ASIGNACIÓN

    @Test
    fun `test formatter with simple numeric assignation expression`() {
        val input = "x=2"
        val tokens: List<Token> = lexer.execute(input)
        val ast: List<ASTNode> = parser.execute(tokens)
        val formatted = formatter.format(ast)
        assertEquals("x = 2;", formatted)
    }

    @Test
    fun `test formatter with simple text assignation expression`() {
        val input = "x=\"hola\""
        val tokens: List<Token> = lexer.execute(input)
        val ast: List<ASTNode> = parser.execute(tokens)
        val formatted = formatter.format(ast)
        assertEquals("x = \"hola\";", formatted)
    }

    // PRINTLN

    @Test
    fun `test formatter with simple print expression`() {
        val input = "println(x)"
        val tokens: List<Token> = lexer.execute(input)
        val ast: List<ASTNode> = parser.execute(tokens)
        val formatted = formatter.format(ast)
        assertEquals("println(x);", formatted)
    }

    @Test
    fun `test formatter with simple print expression with complex operation`() {
        val input = "println(x+2-y*1000)"
        val tokens: List<Token> = lexer.execute(input)
        val ast: List<ASTNode> = parser.execute(tokens)
        val formatted = formatter.format(ast)
        assertEquals("println(x + 2 - y * 1000);", formatted)
    }

    // CONDICIONAL

//    @Test
//    fun `test formatter with conditional expression`(){
//        val input = "if (x>2) {println(x)}"
//        val tokens : List<Token> = lexer.execute(input)
//        val ast : List<ASTNode> = parser.execute(tokens)
//        val formatted = formatter.format(ast)
//        assertEquals("if (x > 2) {\nprintln(x);\n}", formatted)
//    }

//    @Test
//    fun `test canHandle should return true for nodes`() {
//
//    }
}
