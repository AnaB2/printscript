package formatOperations

import Formatter
import ast.ASTNode
import ast.PrintNode

class FormatPrint : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is PrintNode
    }

    override fun format(
        node: ASTNode,
        formatter: Formatter,
    ): String {
        if (!canHandle(node)) error("Node isn't a PrintNode")
        val printNode = node as PrintNode
        val expression = formatter.format(printNode.expression)
        return "println($expression)"
    }
}
