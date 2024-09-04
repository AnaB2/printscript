package formatOperations

import Formatter
import ast.ASTNode

// Interfaz que implementan los formateadores según la operación a formatear
interface FormatOperation {
    fun canHandle(astNode: ASTNode): Boolean;
    fun format(node: ASTNode, formatter: Formatter): String;
}