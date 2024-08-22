package org.example

import Parser
import ast.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import token.Token
import token.TokenPosition
import token.TokenType

class ParserTests {

    @Test
    fun `test parser execution with assignation`() {
        val tokens = listOf(
            Token(TokenType.IDENTIFIER, "var", TokenPosition(0, 4), TokenPosition(1, 9)),
            Token(TokenType.ASSIGNATION, "=", TokenPosition(0, 19), TokenPosition(1, 20)),
            Token(TokenType.LITERAL, "hello", TokenPosition(0, 21), TokenPosition(1, 27)),
            Token(TokenType.PUNCTUATOR, ";", TokenPosition(0, 28), TokenPosition(1, 29))
        )

        val parser = Parser()
        val abstractSyntaxTrees = parser.execute(tokens)

        val assignationNode = abstractSyntaxTrees[0] as AssignationNode

        assertEquals("var", assignationNode.id)
        assertEquals("hello", (assignationNode.expression as LiteralNode).value)
    }

    @Test
    fun `test parser execution with declaration`() {
        val tokens = listOf(
            Token(TokenType.KEYWORD, "let", TokenPosition(0, 0), TokenPosition(1, 3)),
            Token(TokenType.IDENTIFIER, "x", TokenPosition(0, 4), TokenPosition(1, 5)),
            Token(TokenType.DECLARATOR, ":", TokenPosition(0, 6), TokenPosition(1, 7)),
            Token(TokenType.DATA_TYPE, "number", TokenPosition(0, 8), TokenPosition(1, 14)),
            Token(TokenType.PUNCTUATOR, ";", TokenPosition(0, 15), TokenPosition(1, 16))
        )

        val parser = Parser()
        val asts = parser.execute(tokens)

        assertEquals(1, asts.size)

        val ast = asts[0] as DeclarationNode
        val rightNode = ast.expr as LiteralNode

        assertEquals(TokenType.KEYWORD, ast.declType)
        assertEquals("x", ast.id)
        assertEquals(TokenType.DATA_TYPE, ast.valType)
        assertEquals("number", rightNode.value)
    }

    @Test
    fun `test parser execution with println`() {
        val tokens = listOf(
            Token(TokenType.FUNCTION, "println", TokenPosition(0, 0), TokenPosition(1, 6)),
            Token(TokenType.PUNCTUATOR, "(", TokenPosition(0, 7), TokenPosition(1, 8)),
            Token(TokenType.LITERAL, "Hello", TokenPosition(0, 9), TokenPosition(1, 15)),
            Token(TokenType.OPERATOR, "+", TokenPosition(0, 16), TokenPosition(1, 15)),
            Token(TokenType.LITERAL, "world", TokenPosition(0, 16), TokenPosition(1, 15)),
            Token(TokenType.PUNCTUATOR, ")", TokenPosition(0, 22), TokenPosition(1, 23)),
            Token(TokenType.PUNCTUATOR, ";", TokenPosition(0, 24), TokenPosition(1, 25))
        )

        val parser = Parser()
        val abstractSyntaxTrees = parser.execute(tokens)

        val ast = abstractSyntaxTrees[0] as PrintNode
        val expressionNode = ast.expression as BinaryNode

        assertEquals("println", tokens[0].getValue())
        assertEquals("Hello", (expressionNode.left as LiteralNode).value)
        assertEquals("world", (expressionNode.right as LiteralNode).value)
    }

    @Test
    fun `test parser execution with println and multiple concatenations`() {
        val tokens = listOf(
            Token(TokenType.FUNCTION, "println", TokenPosition(0, 0), TokenPosition(1, 6)),
            Token(TokenType.PUNCTUATOR, "(", TokenPosition(0, 7), TokenPosition(1, 8)),
            Token(TokenType.LITERAL, "Hello", TokenPosition(0, 9), TokenPosition(1, 15)),
            Token(TokenType.OPERATOR, "+", TokenPosition(0, 16), TokenPosition(1, 15)),
            Token(TokenType.LITERAL, "world", TokenPosition(0, 16), TokenPosition(1, 15)),
            Token(TokenType.OPERATOR, "+", TokenPosition(0, 16), TokenPosition(1, 15)),
            Token(TokenType.LITERAL, "!", TokenPosition(0, 16), TokenPosition(1, 15)),
            Token(TokenType.PUNCTUATOR, ")", TokenPosition(0, 22), TokenPosition(1, 23)),
            Token(TokenType.PUNCTUATOR, ";", TokenPosition(0, 24), TokenPosition(1, 25))
        )

        val parser = Parser()
        val abstractSyntaxTrees = parser.execute(tokens)

        val ast = abstractSyntaxTrees[0] as PrintNode
        val rightNode = ast.expression as BinaryNode

        assertEquals(TokenType.FUNCTION, tokens[0].getType())
        assertEquals("Hello", (rightNode.left as LiteralNode).value)
        assertEquals(TokenType.OPERATOR, rightNode.operator)

        val innerRightNode = rightNode.right as BinaryNode
        assertEquals("world", (innerRightNode.left as LiteralNode).value)
        assertEquals("!", (innerRightNode.right as LiteralNode).value)
    }
}
