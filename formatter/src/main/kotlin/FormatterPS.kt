import ast.ASTNode
import formatOperations.FormatOperation
import rules.RulesReader

class FormatterPS(
    private val rulesPath : String,
    private val formatOperations : List<FormatOperation>,
) : Formatter {
    private val rules : Map<String, Any> = RulesReader().readFile(rulesPath)

    override fun format(astNodeList: List<ASTNode>): String {
        val result : List<String> = astNodeList.map { astNode -> formatNode(astNode)}
        return result.joinToString("\n") // personalizar saltos de linea según reglas
    }

    private fun formatNode(node: ASTNode): String {
        val formatter = formatOperations.find { it -> it.canHandle(node) }
        return formatter?.format(node) ?: ""
    }

    override fun getRules(): Map<String, Any> {
        return rules;
    }
}