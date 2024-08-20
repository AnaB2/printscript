package factories

import ASTFactory
import ast.AssignationNode
import ast.AstNode
import ast.LiteralNode
import ast.NilNode
import token.Token
import token.TokenType

class AssignationFactory : ASTFactory {
    override fun createAST(tokens: List<Token>): AstNode {
        val assignationToken = tokens.find { it.getType() == TokenType.ASSIGNATION }!!
        val leftTokens = getLeftTokens(tokens)
        val rightTokens = getRightTokens(tokens, leftTokens)
        val leftNode = if (leftTokens.size > 1) {
            variableDeclaration(leftTokens)
        } else {
            createLiteralNode(leftTokens[0])
        }
        val rightNode = if (rightTokens.isEmpty()) {
            NilNode
        } else if (rightTokens.size > 1) {
            createAssignedTree(rightTokens)
        } else {
            createLiteralNode(rightTokens[0])
        }

        return AssignationNode(
            id = leftNode.toString(),
            expression = rightNode,
            valType = assignationToken.getType(),
            position = assignationToken.getPosition()
        )
    }

    private fun createLiteralNode(token: Token): AstNode {
        return LiteralNode(value = token.getValue(), type = token.getType(), position = token.getPosition())
    }

    private fun getRightTokens(tokens: List<Token>, leftTokens: List<Token>) = tokens.drop(leftTokens.size + 1)

    private fun getLeftTokens(tokens: List<Token>) = tokens.takeWhile { it.getValue() != "=" }

    private fun createAssignedTree(tokens: List<Token>): AstNode {
        return if (tokens.any { it.getType() == TokenType.FUNCTION }) {
            FunctionFactory().createAST(tokens)
        } else {
            OperationFactory().createAST(tokens)
        }
    }

    private fun variableDeclaration(tokens: List<Token>): AstNode {
        return DeclarationFactory().createAST(tokens)
    }

    override fun canHandle(tokens: List<Token>): Boolean {
        return tokens.any { it.getType() == TokenType.ASSIGNATION }
    }
}
