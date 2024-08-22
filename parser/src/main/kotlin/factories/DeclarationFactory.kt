package factories

import ASTFactory
import ast.ASTNode
import ast.DeclarationNode
import ast.LiteralNode
import token.Token
import token.TokenType

class DeclarationFactory : ASTFactory {
    override fun createAST(tokens: List<Token>): ASTNode {
        val keywordToken = tokens.find { it.getType() == TokenType.KEYWORD }!!
        val identifierToken = tokens.find { it.getType() == TokenType.IDENTIFIER }!!
        val declaratorToken = tokens.find { it.getType() == TokenType.DECLARATOR }!!
        val dataTypeToken = tokens.find { it.getType() == TokenType.DATA_TYPE }!!

        val exprNode = LiteralNode(
            value = dataTypeToken.getValue(),
            type = dataTypeToken.getType(),
            position = dataTypeToken.getPosition()
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
