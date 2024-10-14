package ast

import token.Token
import token.TokenType

class Tokenizer {
    fun parseToTokens(astNodes: List<ASTNode>): List<List<Token>> {
        val tokens = mutableListOf<List<Token>>()
        for (node in astNodes) {
            val extractedTokens = extractTokensFromAST(node)
            println("Tokens from AST Node: $extractedTokens") // Debugging output
            tokens.add(extractedTokens)
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
                // Change from DECLARATOR to IDENTIFIER
                tokens.add(Token(TokenType.IDENTIFIER, node.id, node.position, node.position))
                traverseAST(node.expr, tokens)
            }
            is AssignationNode -> {
                // Change from DECLARATOR to IDENTIFIER
                tokens.add(Token(TokenType.IDENTIFIER, node.id, node.position, node.position))
                traverseAST(node.expression, tokens)
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
                traverseAST(node.condition, tokens)
                traverseAST(node.thenBlock, tokens)

                // Handle elseBlock if it is not null
                node.elseBlock?.let {
                    val elseToken =
                        Token(
                            type = TokenType.CONDITIONAL,
                            value = "else",
                            initialPosition = it.position,
                            finalPosition = it.position,
                        )
                    tokens.add(elseToken)
                    traverseAST(it, tokens)
                }
            }
            is FunctionNode -> {
                val token =
                    Token(
                        type = TokenType.FUNCTION,
                        value = node.functionName.lowercase(),
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
