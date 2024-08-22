package factories

import ASTFactory
import ast.DeclarationNode
import ast.LiteralNode
import ast.ASTNode
import token.Token
import token.TokenType

class DeclarationFactory : ASTFactory {
    override fun createAST(tokens: List<Token>): ASTNode {
        val keywordToken = tokens.find { it.getType() == TokenType.KEYWORD }
            ?: throw IllegalArgumentException("Expected a KEYWORD token but found none.")
        val identifierToken = tokens.find { it.getType() == TokenType.IDENTIFIER }
            ?: throw IllegalArgumentException("Expected an IDENTIFIER token but found none.")
        val dataTypeToken = tokens.find { it.getType() == TokenType.DATA_TYPE || it.getType() == TokenType.TYPE_OF_DATA }
            ?: throw IllegalArgumentException("Expected a DATA_TYPE or TYPE_OF_DATA token but found none.")
        val expressionToken = tokens.last() // Assuming the last token is the expression

        val exprNode = LiteralNode(
            value = expressionToken.getValue(),
            type = expressionToken.getType(),
            position = expressionToken.getPosition()
        )

        return DeclarationNode(
            declType = keywordToken.getType(),
            id = identifierToken.getValue(),
            valType = dataTypeToken.getType(),
            expr = exprNode,
            position = keywordToken.getPosition()
        )
    }

    override fun canHandle(tokens: List<Token>): Boolean {
        return tokens.any { it.getType() == TokenType.KEYWORD }
    }
}
