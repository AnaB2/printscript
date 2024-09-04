package formatOperations

import Formatter
import ast.ASTNode
import ast.BinaryNode
import ast.LiteralNode

class FormatLiteral : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is LiteralNode
    }

    override fun format(node: ASTNode, formatter: Formatter): String {
        if(!canHandle(node)) error("Node isn't a LiteralNode") else node as LiteralNode
        return node.value
    }
}