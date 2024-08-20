package tests

import Parser
import ast.*
import token.Token
import token.TokenType
import token.TokenPosition
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ParserTest {

    @Test
    fun testCreateASTForAssignation() {
        val tokens = listOf(
            Token(TokenType.IDENTIFIER, "x", TokenPosition(1, 1), TokenPosition(1, 2)),
            Token(TokenType.ASSIGNATION, "=", TokenPosition(1, 2), TokenPosition(1, 3)),
            Token(TokenType.NUMBER, "42", TokenPosition(1, 3), TokenPosition(1, 5))
        )
        val parser = Parser()
        val astNodes = parser.execute(tokens)

        assertEquals(1, astNodes.size)
        assertTrue(astNodes[0] is AssignationNode)

        val assignationNode = astNodes[0] as AssignationNode
        assertEquals("x", assignationNode.id)
        assertEquals("42", (assignationNode.expression as LiteralNode).value)
    }

    @Test
    fun testCreateASTForConditional() {
        val tokens = listOf(
            Token(TokenType.CONDITIONAL, "if", TokenPosition(1, 1), TokenPosition(1, 2)),
            Token(TokenType.PARENTHESIS, "(", TokenPosition(1, 2), TokenPosition(1, 3)),
            Token(TokenType.IDENTIFIER, "x", TokenPosition(1, 3), TokenPosition(1, 4)),
            Token(TokenType.OPERATOR, ">", TokenPosition(1, 4), TokenPosition(1, 5)),
            Token(TokenType.NUMBER, "10", TokenPosition(1, 5), TokenPosition(1, 7)),
            Token(TokenType.PARENTHESIS, ")", TokenPosition(1, 7), TokenPosition(1, 8)),
            Token(TokenType.BLOCK_START, "{", TokenPosition(1, 8), TokenPosition(1, 9)),
            Token(TokenType.IDENTIFIER, "y", TokenPosition(2, 1), TokenPosition(2, 2)),
            Token(TokenType.ASSIGNATION, "=", TokenPosition(2, 2), TokenPosition(2, 3)),
            Token(TokenType.NUMBER, "20", TokenPosition(2, 3), TokenPosition(2, 5)),
            Token(TokenType.BLOCK_END, "}", TokenPosition(3, 1), TokenPosition(3, 2))
        )
        val parser = Parser()
        val astNodes = parser.execute(tokens)

        assertEquals(1, astNodes.size)
        assertTrue(astNodes[0] is ConditionalNode)

        val conditionalNode = astNodes[0] as ConditionalNode
        assertEquals("x", (conditionalNode.condition).value)
    }

    @Test
    fun testCreateASTForDeclaration() {
        val tokens = listOf(
            Token(TokenType.KEYWORD, "let", TokenPosition(1, 1), TokenPosition(1, 4)),
            Token(TokenType.IDENTIFIER, "x", TokenPosition(1, 4), TokenPosition(1, 5)),
            Token(TokenType.DATA_TYPE, "int", TokenPosition(1, 5), TokenPosition(1, 8)),
            Token(TokenType.ASSIGNATION, "=", TokenPosition(1, 8), TokenPosition(1, 9)),
            Token(TokenType.NUMBER, "42", TokenPosition(1, 9), TokenPosition(1, 11))
        )
        val parser = Parser()
        val astNodes = parser.execute(tokens)

        assertEquals(1, astNodes.size)
        assertTrue(astNodes[0] is DeclarationNode)

        val declarationNode = astNodes[0] as DeclarationNode
        assertEquals("x", declarationNode.id)
        assertEquals("int", declarationNode.valType.toString())
        assertEquals("42", (declarationNode.expr as LiteralNode).value)
    }

    @Test
    fun testCreateASTForPrintln() {
        val tokens = listOf(
            Token(TokenType.FUNCTION, "println", TokenPosition(1, 1), TokenPosition(1, 8)),
            Token(TokenType.PARENTHESIS, "(", TokenPosition(1, 8), TokenPosition(1, 9)),
            Token(TokenType.STRING, "\"Hello, World!\"", TokenPosition(1, 9), TokenPosition(1, 24)),
            Token(TokenType.PARENTHESIS, ")", TokenPosition(1, 24), TokenPosition(1, 25))
        )
        val parser = Parser()
        val astNodes = parser.execute(tokens)

        assertEquals(1, astNodes.size)
        assertTrue(astNodes[0] is PrintNode)

        val printNode = astNodes[0] as PrintNode
        assertEquals("\"Hello, World!\"", (printNode.expression as LiteralNode).value)
    }

    @Test
    fun testHandleEmptyTokens() {
        val tokens = emptyList<Token>()
        val parser = Parser()
        assertThrows(Exception::class.java) {
            parser.execute(tokens)
        }
    }

    @Test
    fun testThrowsExceptionForInvalidTokens() {
        val tokens = listOf(
            Token(TokenType.IDENTIFIER, "x", TokenPosition(1, 1), TokenPosition(1, 2)),
            Token(TokenType.OPERATOR, ">", TokenPosition(1, 2), TokenPosition(1, 3)),
            Token(TokenType.ASSIGNATION, "=", TokenPosition(1, 3), TokenPosition(1, 4))
        )
        val parser = Parser()
        assertThrows(Exception::class.java) {
            parser.execute(tokens)
        }
    }

    @Test
    fun testCreateASTForNestedConditionals() {
        val tokens = listOf(
            Token(TokenType.CONDITIONAL, "if", TokenPosition(1, 1), TokenPosition(1, 2)),
            Token(TokenType.PARENTHESIS, "(", TokenPosition(1, 2), TokenPosition(1, 3)),
            Token(TokenType.IDENTIFIER, "x", TokenPosition(1, 3), TokenPosition(1, 4)),
            Token(TokenType.OPERATOR, ">", TokenPosition(1, 4), TokenPosition(1, 5)),
            Token(TokenType.NUMBER, "10", TokenPosition(1, 5), TokenPosition(1, 7)),
            Token(TokenType.PARENTHESIS, ")", TokenPosition(1, 7), TokenPosition(1, 8)),
            Token(TokenType.BLOCK_START, "{", TokenPosition(1, 8), TokenPosition(1, 9)),
            Token(TokenType.CONDITIONAL, "if", TokenPosition(2, 1), TokenPosition(2, 2)),
            Token(TokenType.PARENTHESIS, "(", TokenPosition(2, 2), TokenPosition(2, 3)),
            Token(TokenType.IDENTIFIER, "y", TokenPosition(2, 3), TokenPosition(2, 4)),
            Token(TokenType.OPERATOR, "==", TokenPosition(2, 4), TokenPosition(2, 6)),
            Token(TokenType.NUMBER, "5", TokenPosition(2, 6), TokenPosition(2, 7)),
            Token(TokenType.PARENTHESIS, ")", TokenPosition(2, 7), TokenPosition(2, 8)),
            Token(TokenType.BLOCK_START, "{", TokenPosition(2, 8), TokenPosition(2, 9)),
            Token(TokenType.IDENTIFIER, "z", TokenPosition(3, 1), TokenPosition(3, 2)),
            Token(TokenType.ASSIGNATION, "=", TokenPosition(3, 2), TokenPosition(3, 3)),
            Token(TokenType.NUMBER, "20", TokenPosition(3, 3), TokenPosition(3, 5)),
            Token(TokenType.BLOCK_END, "}", TokenPosition(4, 1), TokenPosition(4, 2)),
            Token(TokenType.BLOCK_END, "}", TokenPosition(5, 1), TokenPosition(5, 2))
        )
        val parser = Parser()
        val astNodes = parser.execute(tokens)

        assertEquals(1, astNodes.size)
        assertTrue(astNodes[0] is ConditionalNode)

        val outerConditional = astNodes[0] as ConditionalNode
        assertTrue(outerConditional.thenBlock is BlockNode)
        val innerBlock = outerConditional.thenBlock as BlockNode
        assertTrue(innerBlock.nodes[0] is ConditionalNode)
    }
}
