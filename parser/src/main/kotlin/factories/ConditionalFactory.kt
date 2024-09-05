package factories

import ast.ASTNode
import ast.BlockNode
import ast.ConditionalNode
import ast.LiteralNode
import ast.NilNode
import token.Token
import token.TokenType
import Parser

class ConditionalFactory : ASTFactory {
    override fun createAST(tokens: List<Token>): ASTNode {
        val conditionToken = tokens.find { it.getType() == TokenType.CONDITIONAL && it.value == "if" }!!
        val conditionNode = LiteralNode(
            value = conditionToken.value,
            type = conditionToken.getType(),
            position = conditionToken.getPosition()
        )
        val thenBlock = parseBlock(tokens, "if")
        val elseBlock = parseBlock(tokens, "else") ?: NilNode

        return ConditionalNode(
            condition = conditionNode,
            thenBlock = thenBlock ?: NilNode,
            elseBlock = elseBlock ?: NilNode,
            position = conditionNode.position
        )
    }

    private fun parseBlock(tokens: List<Token>, blockType: String): ASTNode? {
        val startIndex = tokens.indexOfFirst { it.value == blockType } + 1
        val endIndex = tokens.indexOfFirst { it.value == "}" && it.getType() == TokenType.PUNCTUATOR }
        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            val parser = Parser()
            val blockNodes = parser.execute(tokens.subList(startIndex, endIndex))
            BlockNode(nodes = blockNodes, position = tokens[startIndex].getPosition())
        } else {
            null
        }
    }

    override fun canHandle(tokens: List<Token>): Boolean {
        return tokens.any { it.getType() == TokenType.CONDITIONAL && it.value == "if" }
    }
}
