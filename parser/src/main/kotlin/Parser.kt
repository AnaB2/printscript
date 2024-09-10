import ast.ASTNode
import factories.ASTFactory
import factories.AssignationFactory
import factories.ConditionalFactory
import factories.DeclarationFactory
import factories.PrintlnFactory
import token.Token

class Parser {
    private val factories: List<ASTFactory> =
        listOf(
            ConditionalFactory(),
            PrintlnFactory(),
            DeclarationFactory(),
            AssignationFactory(),
        )

    fun execute(tokens: List<Token>): List<ASTNode> {
        val result = mutableListOf<ASTNode>()
        val sameLineTokens = getSameLineTokens(tokens)
        for (tokenList in sameLineTokens) {
            val astFactory = determineFactory(tokenList)
            if (astFactory != null) {
                result.add(astFactory.createAST(tokenList))
            } else {
                throw Exception("Can't handle this sentence")
            }
        }
        return result
    }

    private fun determineFactory(tokens: List<Token>): ASTFactory? {
        return factories.find { it.canHandle(tokens) }
    }

    private fun getSameLineTokens(tokenList: List<Token>): List<List<Token>> {
        val rows = mutableListOf<List<Token>>()
        var singleRow = mutableListOf<Token>()
        for (token in tokenList) {
            if (token.value != ";" && token.value != "\n") {
                singleRow.add(token)
            } else {
                rows.add(singleRow)
                singleRow = mutableListOf()
            }
        }
        if (singleRow.isNotEmpty()) {
            rows.add(singleRow) // Agregar la última línea si no está vacía
        }
        if (rows.isEmpty()) {
            throw Exception("Error: Not valid code.")
        }
        return rows
    }
}
