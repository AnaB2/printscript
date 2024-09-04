import ast.ASTNode

interface Formatter {
    fun format(astNodes: List<ASTNode>): String

    fun format(astNode: ASTNode): String

    fun getRules(): Map<String, Any>
}
