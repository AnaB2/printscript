import ast.ASTNode

interface Formatter {
    fun formatter(astNode: ASTNode): String

    fun getRules(): Map<String, Any>
}
