package formatter

import ast.ASTNode
import formatOperations.FormatOperation
import formatOperations.commons.HandleLineBreak
import formatOperations.commons.HandleSemicolon
import rules.RulesReader

class FormatterPS : Formatter {
    private val rulesPath: String
    private val formatOperations: List<FormatOperation>
    private val rulesReader: RulesReader
    private val handleSemicolon = HandleSemicolon()
    private val handleLineBreak = HandleLineBreak()

    constructor(rulesPath: String, formatOperations: List<FormatOperation>) {
        this.rulesPath = rulesPath
        this.formatOperations = formatOperations
        rulesReader =
            RulesReader(
                mapOf(
                    "spaceBeforeColon" to Boolean::class,
                    "spaceAfterColon" to Boolean::class,
                    "spaceAroundEquals" to Boolean::class,
                    "lineBreak" to Int::class,
                ),
            )
    }

    override fun format(astNodes: List<ASTNode>): String {
        // formatear nodos de la lista
        val formatedNodes: List<String> = astNodes.map { node -> formatNode(node) } // formatear cada nodo
        val formatedNodesWithSemicolon =
            formatedNodes.map {
                    line ->
                handleSemicolon.handleSemicolon(line)
            } // manejar punto y coma de cada linea

        // añade saltos de línea y devuelve String
        val numberOfLineBreak = rulesReader.readFile(rulesPath)["lineBreak"] as Int
        val result = handleLineBreak.handleLineBreak(formatedNodesWithSemicolon, numberOfLineBreak) // manejar saltos de linea

        return result
    }

    override fun format(astNode: ASTNode): String {
        return formatNode(astNode)
    }

    private fun formatNode(node: ASTNode): String {
        val formatter = formatOperations.find { it -> it.canHandle(node) }
        return formatter?.format(node, this) ?: ""
    }

    override fun getRules(): Map<String, Any> {
        return rulesReader.readFile(rulesPath)
    }
}
