package formatOperations

import ast.ASTNode
import ast.BinaryNode
import ast.LiteralNode

class FormatLiteral : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is LiteralNode
    }

    override fun format(astNode: ASTNode): String {
        TODO("Not yet implemented")
    }
}