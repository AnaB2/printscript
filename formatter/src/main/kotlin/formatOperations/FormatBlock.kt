package formatOperations

import ast.ASTNode
import ast.BinaryNode
import ast.BlockNode

class FormatBlock : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is BlockNode
    }

    override fun format(astNode: ASTNode): String {
        TODO("Not yet implemented")
    }
}