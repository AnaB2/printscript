package formatOperations

import Formatter
import ast.ASTNode
import ast.BinaryNode
import ast.DeclarationNode
import formatOperations.commons.HandleSpace

class FormatDeclaration : FormatOperation {
    private val handleSpace : HandleSpace = HandleSpace()

    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is DeclarationNode
    }

    override fun format(node: ASTNode, formatter: Formatter): String {
        if (!canHandle(node)) error("Node isn't a DeclarationNode") else node as DeclarationNode
        val declType = node.declType
        val id = node.id
        val valType = node.valType
        val expr = formatter.format(node.expr)

        // handle spaces
        val spaceBeforeColon = formatter.getRules()["spaceBeforeColon"] as Boolean
        val spaceAfterColon = formatter.getRules()["spaceAfterColon"] as Boolean
        val spaceAroundEquals = formatter.getRules()["spaceAroundEquals"] as Boolean

        val equal = handleSpace.handleSpace("=", spaceAroundEquals, spaceAroundEquals)
        val colon = handleSpace.handleSpace(":", spaceBeforeColon, spaceAfterColon)

        return "$declType $id$colon$valType$equal$expr"
    }
}