package formatOperations

import ast.ASTNode
import ast.BinaryNode
import ast.PrintNode

class FormatPrint : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is PrintNode
    }

    override fun format(astNode: ASTNode): String {
        TODO("Not yet implemented")
    }
}