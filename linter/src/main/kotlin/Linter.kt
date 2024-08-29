import ast.ASTNode

interface Linter {

    fun readJsonFile(filePath: String): String

    fun check(trees: List<ASTNode>)
}
