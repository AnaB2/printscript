package formatOperations

import Formatter
import ast.ASTNode
import ast.BinaryNode
import formatOperations.commons.HandleSpace

class FormatBinary : FormatOperation {
    private val handleSpace : HandleSpace = HandleSpace()

    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is BinaryNode
    }

    override fun format(node: ASTNode, formatter: Formatter): String {
        if (!canHandle(node)) error("Node isn't a BinaryNode") else node as BinaryNode
        val left = formatter.format(node.left)
        val right = formatter.format(node.right)
        return "$left ${node.operator} $right"
    }
}