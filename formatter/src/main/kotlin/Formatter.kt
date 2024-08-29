import ast.ASTNode

interface Formatter {
    fun format(astNodeList: List<ASTNode>) : String;
    fun getRules() : Map<String, Any>;
}