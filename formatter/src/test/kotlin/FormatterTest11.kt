import formatter.FormatterBuilderPS
import lexer.Lexer
import org.example.lexer.TokenMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.Parser

class FormatterTest11 {
    private val yamlPath = "src/test/resources/rules11.yaml"
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
        val formatted = formatter.format(input)
        assertEquals("const x : boolean = true;", formatted)
    }

    // CONDICIONAL

    @Test
    fun `test formatter with conditional expression`() {
        val input = "if (x) { println(\"yes\") } else { println(\"no\") }"
        val formatted = formatter.format(input)
        assertEquals("if (x) {\n   println(\"yes\");\n} else {\n   println(\"no\");\n}", formatted)
    }
}
