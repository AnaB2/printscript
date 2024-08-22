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
        val asts = parser.execute(tokens)

        val assignationNode = asts[0] as AssignationNode

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

//    @Test
//    fun `test parser execution with println`() {
//        val tokens = listOf(
//            Token(TokenType.FUNCTION, "println", TokenPosition(0, 0), TokenPosition(1, 6)),
//            Token(TokenType.PUNCTUATOR, "(", TokenPosition(0, 7), TokenPosition(1, 8)),
//            Token(TokenType.LITERAL, "Hello", TokenPosition(0, 9), TokenPosition(1, 15)),
//            Token(TokenType.OPERATOR, "+", TokenPosition(0, 16), TokenPosition(1, 15)),
//            Token(TokenType.LITERAL, "world", TokenPosition(0, 16), TokenPosition(1, 15)),
//            Token(TokenType.PUNCTUATOR, ")", TokenPosition(0, 22), TokenPosition(1, 23)),
//            Token(TokenType.PUNCTUATOR, ";", TokenPosition(0, 24), TokenPosition(1, 25))
//        )
//
//        val parser = Parser()
//        val asts = parser.execute(tokens)
//
//        val ast = asts[0] as PrintNode
//        val expressionNode = ast.expression as BinaryNode
//
//        assertEquals("println", tokens[0].getValue())
//        assertEquals("Hello", (expressionNode.left as LiteralNode).value)
//        assertEquals("world", (expressionNode.right as LiteralNode).value)
//        assertEquals("+", expressionNode.operator.getValue())
//    }
//
//
//    @Test
//    fun `test parser execution with println and multiple concatenations`() {
//        val tokens = listOf(
//            Token(TokenType.FUNCTION, "println", TokenPosition(0, 0), TokenPosition(1, 6)),
//            Token(TokenType.PUNCTUATOR, "(", TokenPosition(0, 7), TokenPosition(1, 8)),
//            Token(TokenType.LITERAL, "Hello", TokenPosition(0, 9), TokenPosition(1, 15)),
//            Token(TokenType.OPERATOR, "+", TokenPosition(0, 16), TokenPosition(1, 15)),
//            Token(TokenType.LITERAL, "world", TokenPosition(0, 16), TokenPosition(1, 15)),
//            Token(TokenType.OPERATOR, "+", TokenPosition(0, 16), TokenPosition(1, 15)),
//            Token(TokenType.LITERAL, "!", TokenPosition(0, 16), TokenPosition(1, 15)),
//            Token(TokenType.PUNCTUATOR, ")", TokenPosition(0, 22), TokenPosition(1, 23)),
//            Token(TokenType.PUNCTUATOR, ";", TokenPosition(0, 24), TokenPosition(1, 25))
//        )
//
//        val parser = Parser()
//        val abstractSyntaxTrees = parser.execute(tokens)
//
//        val ast = abstractSyntaxTrees[0] as PrintNode
//        val rightNode = ast.expression as BinaryNode
//
//        assertEquals(TokenType.FUNCTION, tokens[0].getType())
//        assertEquals("Hello", (rightNode.left as LiteralNode).value)
//
//        assertEquals(TokenType.OPERATOR, rightNode.operator.getType())
//
//        val innerRightNode = rightNode.right as BinaryNode
//        assertEquals("world", (innerRightNode.left as LiteralNode).value)
//        assertEquals("!", (innerRightNode.right as LiteralNode).value)
//    }

    @Test
    fun `test parser execution with multiple declarations in one line`() {
        val tokens = listOf(
            Token(TokenType.KEYWORD, "let", TokenPosition(0, 0), TokenPosition(1, 3)),
            Token(TokenType.IDENTIFIER, "x", TokenPosition(0, 4), TokenPosition(1, 5)),
            Token(TokenType.DECLARATOR, ":", TokenPosition(0, 6), TokenPosition(1, 7)),
            Token(TokenType.DATA_TYPE, "number", TokenPosition(0, 8), TokenPosition(1, 14)),
            Token(TokenType.PUNCTUATOR, ";", TokenPosition(0, 15), TokenPosition(1, 16)),
            Token(TokenType.KEYWORD, "let", TokenPosition(1, 17), TokenPosition(2, 20)),
            Token(TokenType.IDENTIFIER, "y", TokenPosition(1, 21), TokenPosition(2, 22)),
            Token(TokenType.DECLARATOR, ":", TokenPosition(1, 23), TokenPosition(2, 24)),
            Token(TokenType.DATA_TYPE, "string", TokenPosition(1, 25), TokenPosition(2, 31)),
            Token(TokenType.PUNCTUATOR, ";", TokenPosition(1, 32), TokenPosition(2, 33))
        )

        val parser = Parser()
        val asts = parser.execute(tokens)

        assertEquals(2, asts.size)

        val firstDeclaration = asts[0] as DeclarationNode
        assertEquals("x", firstDeclaration.id)
        assertEquals(TokenType.DATA_TYPE, firstDeclaration.valType)

        val secondDeclaration = asts[1] as DeclarationNode
        assertEquals("y", secondDeclaration.id)
        assertEquals(TokenType.DATA_TYPE, secondDeclaration.valType)
    }

    @Test
    fun `test parser with lexer input, assignation, declaration & println`() {
        val input = """
        let x:number = 42;
        let y:number = 10;
        println(x + y);
    """.trimIndent()

        val tokenMapper = TokenMapper("1.0")
        val lexer = Lexer(tokenMapper)

        val tokens = lexer.execute(input)
        val parser = Parser()
        val trees = parser.execute(tokens)

        assertEquals(3, trees.size)

        val firstTree = trees[0] as DeclarationNode
        assertEquals("x", firstTree.id)
        assertEquals(TokenType.KEYWORD, firstTree.declType)
        assertEquals(TokenType.DATA_TYPE, firstTree.valType)

        val firstRightNode = firstTree.expr as LiteralNode
        assertEquals("42", firstRightNode.value)

        val secondTree = trees[1] as DeclarationNode
        assertEquals("y", secondTree.id)
        assertEquals(TokenType.KEYWORD, secondTree.declType)
        assertEquals(TokenType.DATA_TYPE, secondTree.valType)

        val secondRightNode = secondTree.expr as LiteralNode
        assertEquals("10", secondRightNode.value)

        val thirdTree = trees[2] as PrintNode
        val expressionNode = thirdTree.expression
        if (expressionNode is BinaryNode) {
            assertEquals("+", expressionNode.operator.getValue())
            assertEquals("x", (expressionNode.left as LiteralNode).value)
            assertEquals("y", (expressionNode.right as LiteralNode).value)
        } else if (expressionNode is LiteralNode) {
            assertEquals("x", expressionNode.value)
        }
    }
}
