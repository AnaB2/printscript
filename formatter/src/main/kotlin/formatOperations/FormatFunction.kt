package formatOperations

import ast.ASTNode
import ast.BinaryNode
import ast.FunctionNode

class FormatFunction: FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is FunctionNode
    }

    override fun format(astNode: ASTNode): String {
        TODO("Not yet implemented")
    }
}