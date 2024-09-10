package factories

import ast.ASTNode
import ast.FunctionNode
import ast.LiteralNode
import token.Token
import token.TokenType

class FunctionFactory : ASTFactory {
    override fun createAST(tokens: List<Token>): ASTNode {
        val functionToken = tokens.find { it.getType() == TokenType.FUNCTION }!!
        val expressionToken = tokens.last()

        val expressionNode =
            LiteralNode(
                value = expressionToken.value,
                type = expressionToken.getType(),
                position = expressionToken.getPosition(),
            )

        return FunctionNode(
            function = functionToken.getType(),
            expression = expressionNode,
            position = functionToken.getPosition(),
        )
    }

    override fun canHandle(tokens: List<Token>): Boolean {
        return tokens.any { it.getType() == TokenType.FUNCTION }
    }
}
