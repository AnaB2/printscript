package formatOperations

import Formatter
import ast.ASTNode
import ast.PrintNode

class FormatPrint : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is PrintNode
    }

    override fun format(node: ASTNode, formatter: Formatter): String {
        if(!canHandle(node)) error("Node isn't a PrintNode") else node as PrintNode
        val expression = formatter.format(node.expression)
        return "print($expression)"
    }
}