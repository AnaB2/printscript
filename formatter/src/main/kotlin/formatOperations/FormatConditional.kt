package formatOperations

import Formatter
import ast.ASTNode
import ast.BinaryNode
import ast.BlockNode
import ast.ConditionalNode

class FormatConditional : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is ConditionalNode
    }

    override fun format(node: ASTNode, formatter: Formatter): String {
        if(!canHandle(node)) error("Node isn't a ConditionalNode") else node as ConditionalNode
        val condition = formatter.format(node.condition)
        val thenBlock = formatter.format(node.thenBlock)
        val elseBlock = formatter.format(node.elseBlock)
        return "if ($condition) {\n$thenBlock\n} else {\n$elseBlock\n}"
    }
}