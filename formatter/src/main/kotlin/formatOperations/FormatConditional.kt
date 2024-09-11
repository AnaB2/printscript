package formatOperations

import ast.ASTNode
import ast.ConditionalNode
import formatter.Formatter

class FormatConditional : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is ConditionalNode
    }

    override fun format(
        node: ASTNode,
        formatter: Formatter,
    ): String {
        if (!canHandle(node)) error("Node isn't a ConditionalNode")
        val conditionalNode = node as ConditionalNode
        val condition = formatter.format(conditionalNode.condition)
        val thenBlock = formatter.format(conditionalNode.thenBlock)
        val elseBlock = formatter.format(conditionalNode.elseBlock)
        return "if ($condition) {\n$thenBlock\n} else {\n$elseBlock\n}"
    }
}
