package formatOperations

import ast.ASTNode
import formatter.Formatter

interface FormatOperation {
    fun canHandle(astNode: ASTNode): Boolean

    fun format(
        node: ASTNode,
        formatter: Formatter,
    ): String
}
