import ast.ASTNode

interface Formatter {
    fun format(astNode: List<ASTNode>): String

    fun getRules(): Map<String, Any>
}
