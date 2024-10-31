import lexer.TokenMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import token.TokenType

class TokenMapperTest {
    @Test
    fun `test initialize with version 1,0`() {
        val tokenMapper = TokenMapper("1.0")
        assertNotNull(tokenMapper.getStrategyMap())
        assertEquals(11, tokenMapper.getStrategyMap().size) // Should have 10 strategies for version 1.0
    }

    @Test
    fun `test initialize with version 1,1`() {
        val tokenMapper = TokenMapper("1.1")
        assertNotNull(tokenMapper.getStrategyMap())
        assertEquals(13, tokenMapper.getStrategyMap().size) // Should have 13 strategies for version 1.1
    }

    @Test
    fun `test classify reserved keywords in version 1,0`() {
        val tokenMapper = TokenMapper("1.0")
        assertEquals(TokenType.KEYWORD, tokenMapper.classify("let"))
        assertEquals(TokenType.FUNCTION, tokenMapper.classify("println"))
        assertEquals(TokenType.IDENTIFIER, tokenMapper.classify("randomKeyword"))
    }

    @Test
    fun `test classify reserved keywords in version 1,1`() {
        val tokenMapper = TokenMapper("1.1")
        assertEquals(TokenType.KEYWORD, tokenMapper.classify("const"))
        assertEquals(TokenType.CONDITIONAL, tokenMapper.classify("if"))
        assertEquals(TokenType.BOOLEANLITERAL, tokenMapper.classify("true"))
        assertEquals(TokenType.IDENTIFIER, tokenMapper.classify("randomKeyword"))
    }

    @Test
    fun `test classify number literal`() {
        val tokenMapper = TokenMapper("1.0")
        assertEquals(TokenType.NUMBERLITERAL, tokenMapper.classify("123"))
        assertEquals(TokenType.NUMBERLITERAL, tokenMapper.classify("123.45"))
        assertEquals(TokenType.UNKNOWN, tokenMapper.classify("123a"))
    }

    @Test
    fun `test classify string literal`() {
        val tokenMapper = TokenMapper("1.0")
        assertEquals(TokenType.STRINGLITERAL, tokenMapper.classify("\"Hello World\""))
        assertEquals(TokenType.STRINGLITERAL, tokenMapper.classify("'Single Quote'"))
        assertEquals(TokenType.UNKNOWN, tokenMapper.classify("\"Unclosed String"))
    }

    @Test
    fun `test classify identifier`() {
        val tokenMapper = TokenMapper("1.0")
        assertEquals(TokenType.IDENTIFIER, tokenMapper.classify("myVariable"))
        assertEquals(TokenType.IDENTIFIER, tokenMapper.classify("_underscoreVar"))
        assertEquals(TokenType.UNKNOWN, tokenMapper.classify("123var"))
    }

    @Test
    fun `test classify operator`() {
        val tokenMapper = TokenMapper("1.0")
        assertEquals(TokenType.OPERATOR, tokenMapper.classify("+"))
        assertEquals(TokenType.OPERATOR, tokenMapper.classify("=="))
        assertEquals(TokenType.OPERATOR, tokenMapper.classify("++"))
    }

    @Test
    fun `test classify unknown token`() {
        val tokenMapper = TokenMapper("1.0")
        assertEquals(TokenType.UNKNOWN, tokenMapper.classify(""))
        assertEquals(TokenType.UNKNOWN, tokenMapper.classify("!@#$%"))
    }

    @Test
    fun `test classify const in version 1,0 throws exception`() {
        val tokenMapper = TokenMapper("1.0")
        val exception =
            assertThrows<IllegalArgumentException> {
                tokenMapper.classify("const")
            }
        assertEquals("Const declarations are not allowed in version 1.0", exception.message)
    }

    @Test
    fun `test classify unsupported version throws exception`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                TokenMapper("2.0")
            }
        assertEquals("Unsupported version: 2.0", exception.message)
    }
}
