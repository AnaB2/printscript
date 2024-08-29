import ast.ASTNode

interface Linter {

    fun readJsonFile(filePath: String): String

    fun check(asts: List<ASTNode>)
}
