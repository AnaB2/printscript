package factories

import ast.ASTNode
import ast.BlockNode
import ast.ConditionalNode
import ast.LiteralNode
import ast.NilNode
import parser.Parser
import token.Token
import token.TokenType

class ConditionalFactory : ASTFactory {
    override fun createAST(tokens: List<Token>): ASTNode {
        // Encontrar la condición
        val startCondition = tokens.indexOfFirst { it.value == "(" } + 1
        val conditionToken = tokens[startCondition]
        val conditionNode =
            LiteralNode(
                value = conditionToken.value,
                type = conditionToken.getType(),
                position = conditionToken.getPosition(),
            )

        // Procesar el bloque 'then' del 'if'
        val thenBlock = parseBlock(tokens, "if")

        // Procesar el bloque 'else', si existe
        val elseBlock =
            if (tokens.any { it.value == "else" }) {
                parseBlock(tokens, "else")
            } else {
                NilNode // Si no hay 'else', devolver un 'NilNode'
            }

        return ConditionalNode(
            condition = conditionNode,
            thenBlock = thenBlock ?: NilNode,
            elseBlock = elseBlock ?: NilNode,
            position = conditionNode.position,
        )
    }

    private fun parseBlock(
        allTokens: List<Token>,
        blockType: String,
    ): ASTNode? {
        val index = allTokens.indexOfFirst { it.value == blockType }
        if (index < 0) return null
        val tokens = allTokens.drop(index)

        // Buscar el bloque que empieza con "{" y termina con "}"
        val startBlock = tokens.indexOfFirst { it.getType() == TokenType.PUNCTUATOR && it.value == "{" }
        val endBlock = tokens.indexOfFirst { it.getType() == TokenType.PUNCTUATOR && it.value == "}" }

        // Verificar que haya delimitadores del bloque
        if (startBlock < 0 || endBlock < 0 || endBlock <= startBlock) {
            throw RuntimeException("Error parsing block: missing or unbalanced braces")
        }

        // Procesar los tokens del bloque
        return BlockNode(
            nodes = Parser().execute(tokens.subList(startBlock + 1, endBlock)),
            position = tokens[startBlock + 1].getPosition(),
        )
    }

    override fun canHandle(tokens: List<Token>): Boolean {
        return tokens.any { it.getType() == TokenType.CONDITIONAL && it.value == "if" }
    }
}
