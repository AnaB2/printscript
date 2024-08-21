package factories

import ast.AstNode
import ast.FunctionNode
import token.Token
import token.TokenType
import ASTFactory
import ast.LiteralNode

class FunctionFactory : ASTFactory {
    override fun createAST(tokens: List<Token>): AstNode {
        val functionToken = tokens.find { it.getType() == TokenType.FUNCTION }!!
        val expressionToken = tokens.last()

        val expressionNode = LiteralNode(
            value = expressionToken.getValue(),
            type = expressionToken.getType(),
            position = expressionToken.getPosition()
        )

        return FunctionNode(
            function = functionToken.getType(),
            expression = expressionNode,
            position = functionToken.getPosition()
        )
    }

    override fun canHandle(tokens: List<Token>): Boolean {
        return tokens.any { it.getType() == TokenType.FUNCTION }
    }
}
