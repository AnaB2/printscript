package formatOperations

import ast.ASTNode

// Interfaz que implementan los formateadores según la operación a formatear
interface FormatOperation {
    fun canHandle(astNode: ASTNode): Boolean;
    fun format(astNode: ASTNode): String;
}