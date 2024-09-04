package formatOperations

import Formatter
import ast.ASTNode
import ast.BinaryNode
import ast.BlockNode

class FormatBlock : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is BlockNode
    }

    override fun format(node: ASTNode, formatter: Formatter): String {
        if(!canHandle(node)) error("Node isn't a BlockNode") else node as BlockNode
        return formatter.format(node.nodes)
    }
}