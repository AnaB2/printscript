package formatOperations

import ast.ASTNode
import ast.BinaryNode

class FormatBinary : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is BinaryNode
    }

    override fun format(astNode: ASTNode): String {
        TODO("Not yet implemented")
    }
}