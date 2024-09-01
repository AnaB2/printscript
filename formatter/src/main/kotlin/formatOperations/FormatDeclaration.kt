package formatOperations

import ast.ASTNode
import ast.BinaryNode
import ast.DeclarationNode

class FormatDeclaration : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is DeclarationNode
    }

    override fun format(astNode: ASTNode): String {
        TODO("Not yet implemented")
    }
}