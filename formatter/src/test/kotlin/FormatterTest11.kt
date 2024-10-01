import ast.ASTNode
import formatter.FormatterBuilderPS
import lexer.Lexer
import org.example.lexer.TokenMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.Parser
import token.Token

class FormatterTest11 {
    private val yamlPath = "src/test/resources/rules.yaml"
    private val lexer = Lexer(TokenMapper("1.1"))
    private val parser = Parser()

    private val formatter =
        FormatterBuilderPS().build(
            yamlPath,
            "1.1",
        )

    // DECLARACIÓN CONST DE BOOLEAN

    @Test
    fun `test formatter with simple boolean declaration expression`() {
        val input = "const x:boolean=true"
        val tokens: List<Token> = lexer.execute(input)
        val ast: List<ASTNode> = parser.execute(tokens)
        val formatted = formatter.format(ast)
        assertEquals("const x : boolean = true;", formatted)
    }

    // CONDICIONAL

//    @Test
//    fun `test formatter with conditional expression`(){
//        val input = "if (x>2) {println(\"yes\")} else {println(\"no\")}"
//        val tokens : List<Token> = lexer.execute(input)
//        val ast : List<ASTNode> = parser.execute(tokens)
//        val formatted = formatter.format(ast)
//        assertEquals("if (x > 2) {" +
//                "   println(\"yes\");" +
//                "} else {" +
//                "   println(\"no\")" +
//                "}", formatted)
//    }

    @Test
    fun `test canHandle should return true for nodes`() {
    }
}
