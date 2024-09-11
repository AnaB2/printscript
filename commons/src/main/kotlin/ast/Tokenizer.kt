package ast

import token.Token
import token.TokenType

class Tokenizer {
    fun parseToTokens(astNodes: List<ASTNode>): List<List<Token>> {
        val tokens = mutableListOf<List<Token>>()
        for (node in astNodes) {
            tokens.add(
                extractTokensFromAST(node),
            )
        }
        return tokens
    }

    private fun extractTokensFromAST(node: ASTNode): List<Token> {
        val tokens = mutableListOf<Token>()
        traverseAST(
            node,
            tokens,
        )
        return tokens
    }

    private fun traverseAST(
        node: ASTNode,
        tokens: MutableList<Token>,
    ) {
        when (node) {
            is LiteralNode -> {
                val token =
                    Token(
                        type = node.type,
                        value = node.value,
                        initialPosition = node.position,
                        finalPosition = node.position,
                    )
                tokens.add(token)
            }
            is BinaryNode -> {
                traverseAST(
                    node.left,
                    tokens,
                )
                val token =
                    Token(
                        type = TokenType.OPERATOR,
                        value = node.operator.value,
                        initialPosition = node.position,
                        finalPosition = node.position,
                    )
                tokens.add(token)
                traverseAST(
                    node.right,
                    tokens,
                )
            }
            is PrintNode -> {
                val token =
                    Token(
                        type = TokenType.FUNCTION,
                        value = "print",
                        initialPosition = node.position,
                        finalPosition = node.position,
                    )
                tokens.add(token)
                traverseAST(
                    node.expression,
                    tokens,
                )
            }
            is DeclarationNode -> {
                val token =
                    Token(
                        type = TokenType.DECLARATOR,
                        value = node.id,
                        initialPosition = node.position,
                        finalPosition = node.position,
                    )
                tokens.add(token)
                traverseAST(
                    node.expr,
                    tokens,
                )
            }
            is AssignationNode -> {
                val token =
                    Token(
                        type = TokenType.ASSIGNATION,
                        value = node.id,
                        initialPosition = node.position,
                        finalPosition = node.position,
                    )
                tokens.add(token)
                traverseAST(
                    node.expression,
                    tokens,
                )
            }
            is BlockNode -> {
                node.nodes.forEach {
                    traverseAST(
                        it,
                        tokens,
                    )
                }
            }
            is ConditionalNode -> {
                val ifToken =
                    Token(
                        type = TokenType.CONDITIONAL,
                        value = "if",
                        initialPosition = node.position,
                        finalPosition = node.position,
                    )
                tokens.add(ifToken)
                traverseAST(
                    node.condition,
                    tokens,
                )
                traverseAST(
                    node.thenBlock,
                    tokens,
                )
                if (node.elseBlock != NilNode) {
                    val elseToken =
                        Token(
                            type = TokenType.CONDITIONAL,
                            value = "else",
                            initialPosition = node.elseBlock.position,
                            finalPosition = node.elseBlock.position,
                        )
                    tokens.add(elseToken)
                    traverseAST(
                        node.elseBlock,
                        tokens,
                    )
                }
            }
            is FunctionNode -> {
                val token =
                    Token(
                        type = TokenType.FUNCTION,
                        value = node.function.name.lowercase(),
                        initialPosition = node.position,
                        finalPosition = node.position,
                    )
                tokens.add(token)
                traverseAST(
                    node.expression,
                    tokens,
                )
            }
            is NilNode -> {
                // Handle NilNode if necessary
            }
        }
    }
}
