import formatter.FormatterBuilderPS
import lexer.Lexer
import lexer.TokenMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.Parser

class FormatterTest10 {
    private val yamlPath = "src/test/resources/rules10.yaml"
    private val lexer = Lexer(TokenMapper("1.0"))
    private val parser = Parser()

    private val formatter =
        FormatterBuilderPS().build(
            yamlPath,
            "1.0",
        )

    @Test
    fun `test formatter with simple numeric declaration expression`() {
        val input = "let x:number=2"
        val formatted = formatter.format(input)
        assertEquals("let x : number = 2;", formatted)
    }

    @Test
    fun `test formatter with simple text declaration expression`() {
        val input = "let x:string=\"hola\""
        val formatted = formatter.format(input)
        assertEquals("let x : string = \"hola\";", formatted)
    }

    @Test
    fun `test formatter with simple numeric assignation expression`() {
        val input = "x=2"
        val formatted = formatter.format(input)
        assertEquals("x = 2;", formatted)
    }

    @Test
    fun `test formatter with simple text assignation expression`() {
        val input = "x=\"hola\""
        val formatted = formatter.format(input)
        assertEquals("x = \"hola\";", formatted)
    }

    @Test
    fun `test formatter with simple print expression`() {
        val input = "println(x)"
        val formatted = formatter.format(input)
        assertEquals("println(x);", formatted)
    }

    @Test
    fun `test formatter with simple print expression with complex operation`() {
        val input = "println(x+2-y*1000)"
        val formatted = formatter.format(input)
        assertEquals("println(x + 2 - y * 1000);", formatted)
    }

    @Test
    fun `test formatter with assignation, declaration and println`() {
        val input = """
            let x:number=40+1;
            let y:number=10+1;
            println(x+y);
            """

        val formatted = formatter.format(input)
        assertEquals("let x : number = 40 + 1;\nlet y : number = 10 + 1;\nprintln(x + y);", formatted)
    }
}
