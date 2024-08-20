package factories

import ast.AstNode
import ast.LiteralNode
import ast.PrintNode
import token.Token
import token.TokenType
import ASTFactory

class PrintlnFactory : ASTFactory {
    override fun createAST(tokens: List<Token>): AstNode {
        val printlnToken = tokens.find { it.getValue() == "println" }!!
        val expressionToken = tokens[tokens.indexOf(printlnToken) + 1]

        val expressionNode = LiteralNode(
            value = expressionToken.getValue(),
            type = expressionToken.getType(),
            position = expressionToken.getPosition()
        )

        return PrintNode(
            expression = expressionNode,
            position = printlnToken.getPosition()
        )
    }

    override fun canHandle(tokens: List<Token>): Boolean {
        return tokens.any { it.getValue() == "println" }
    }
}
