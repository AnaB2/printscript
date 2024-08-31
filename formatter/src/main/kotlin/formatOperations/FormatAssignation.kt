package formatOperations

import ast.ASTNode
import ast.AssignationNode

class FormatAssignation : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        astNode is AssignationNode
    }

    override fun format(astNode: ASTNode): String {
        TODO("Not yet implemented")
    }
}