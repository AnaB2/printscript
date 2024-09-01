package formatOperations

import ast.ASTNode
import ast.AssignationNode
import com.sun.jmx.mbeanserver.Util.cast

class FormatAssignation : FormatOperation {
    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is AssignationNode
    }

    override fun format(astNode: ASTNode): String {

    }
}