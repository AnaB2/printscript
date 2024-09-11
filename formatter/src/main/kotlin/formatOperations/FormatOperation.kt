package formatOperations

import ast.ASTNode
import formatter.Formatter

// Interfaz que implementan los formateadores según la operación a formatear
interface FormatOperation {
    fun canHandle(astNode: ASTNode): Boolean

    fun format(
        node: ASTNode,
        formatter: Formatter,
    ): String
}
