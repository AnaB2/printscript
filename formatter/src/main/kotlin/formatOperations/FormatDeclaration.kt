package formatOperations

import Formatter
import ast.ASTNode
import ast.DeclarationNode
import ast.LiteralNode
import formatOperations.commons.HandleSpace
import token.TokenType

class FormatDeclaration : FormatOperation {
    private val handleSpace: HandleSpace = HandleSpace()

    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is DeclarationNode
    }

    override fun format(
        node: ASTNode,
        formatter: Formatter,
    ): String {
        if (!canHandle(node)) error("Node isn't a DeclarationNode")
        val declarationNode = node as DeclarationNode
        val declType = "let"
        val id = declarationNode.id
        val exprNode = declarationNode.expr as LiteralNode
        val valType = if (exprNode.type == TokenType.STRINGLITERAL) "string" else "number"

        // handle spaces
        val spaceBeforeColon = formatter.getRules()["spaceBeforeColon"] as Boolean
        val spaceAfterColon = formatter.getRules()["spaceAfterColon"] as Boolean
        val spaceAroundEquals = formatter.getRules()["spaceAroundEquals"] as Boolean

        val equal = handleSpace.handleSpace("=", spaceAroundEquals, spaceAroundEquals)
        val colon = handleSpace.handleSpace(":", spaceBeforeColon, spaceAfterColon)
        val exprValue = formatter.format(exprNode)

        return "$declType $id$colon$valType$equal$exprValue"
    }
}
