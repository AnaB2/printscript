package formatOperations

import ast.ASTNode
import ast.BinaryNode
import ast.BlockNode
import ast.ConditionalNode

class FormatConditional : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is ConditionalNode
    }

    override fun format(astNode: ASTNode): String {
        TODO("Not yet implemented")
    }
}