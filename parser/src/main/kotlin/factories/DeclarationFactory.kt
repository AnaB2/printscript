package factories

import ast.ASTNode
import ast.DeclarationNode
import ast.LiteralNode
import ast.NilNode
import token.Token
import token.TokenType

class DeclarationFactory : ASTFactory {
    override fun createAST(tokens: List<Token>): ASTNode {
        val keywordToken =
            tokens.find { it.getType() == TokenType.KEYWORD }
                ?: throw IllegalArgumentException("Expected a KEYWORD token but found none.")
        val identifierToken =
            tokens.find { it.getType() == TokenType.IDENTIFIER }
                ?: throw IllegalArgumentException("Expected an IDENTIFIER token but found none.")
        val dataTypeToken =
            tokens.find { it.getType() == TokenType.DATA_TYPE }
                ?: throw IllegalArgumentException("Expected a DATA_TYPE or DATA_TYPE token but found none.")

        // los tokens de la expresión estarán luego del token de asignación, creo LiteralNode o BinaryNode
        val initialPositionExpression = tokens.indexOfFirst { it -> it.value == "=" } + 1
        val expressionTokens: List<Token>? = findExpressionTokens(initialPositionExpression, tokens)

        val expressionNode: ASTNode = if (expressionTokens == null) NilNode else findExpressionNode(expressionTokens)

        val dataTypeValue = dataTypeToken.value
        // chequear si es correcto

        return DeclarationNode(
            declType = keywordToken.getType(),
            declValue = keywordToken.value,
            id = identifierToken.value,
            dataType = dataTypeToken.getType(),
            dataTypeValue = dataTypeValue,
            expr = expressionNode,
            position = keywordToken.getPosition(),
        )
    }

    override fun canHandle(tokens: List<Token>): Boolean {
        return tokens.any { it.getType() == TokenType.KEYWORD }
    }

    private fun findExpressionTokens(
        initialPositionExpression: Int,
        tokens: List<Token>,
    ): List<Token>? {
        if (initialPositionExpression <= 0) return null
        return if (initialPositionExpression != tokens.size) {
            tokens.subList(
                initialPositionExpression,
                tokens.size,
            )
        } else {
            listOf(tokens.last()) // Assuming the last token is the expression
        }
    }

    private fun findExpressionNode(expressionTokens: List<Token>): ASTNode {
        return if (expressionTokens.size == 1) {
            createLiteralNode(
                expressionTokens[0],
            )
        } else {
            OperationFactory().createAST(expressionTokens)
        }
    }

    private fun createLiteralNode(token: Token): ASTNode {
        return LiteralNode(
            value = token.value,
            type = token.getType(),
            position = token.getPosition(),
        )
    }
}
