package ast

import token.TokenPosition
import token.TokenType

sealed class AstNode {
    abstract val position: TokenPosition
}

data class LiteralNode(
    val value: String,
    val type: TokenType,
    override val position: TokenPosition
) : AstNode()

data class BinaryNode(
    val left: AstNode,
    val right: AstNode,
    val operator: TokenType,
    override val position: TokenPosition,
) : AstNode()

data class PrintNode(
    val expression: AstNode,
    override val position: TokenPosition
) : AstNode()

data class DeclarationNode(
    val declType: TokenType,
    val id: String,
    val valType: TokenType,
    val expr: AstNode,
    override val position: TokenPosition,
) : AstNode()

data class AssignationNode(
    val id: String,
    val expression: AstNode,
    val valType: TokenType,
    override val position: TokenPosition,
) : AstNode()

data class BlockNode(
    val nodes: List<AstNode>,
    override val position: TokenPosition
) : AstNode()

data class ConditionalNode(
    val condition: LiteralNode,
    val thenBlock: AstNode,
    val elseBlock: AstNode,
    override val position: TokenPosition,
) : AstNode()

data class FunctionNode(
    val function: TokenType,
    val expression: AstNode,
    override val position: TokenPosition
) : AstNode()

data object NilNode : AstNode() {
    override val position: TokenPosition
        get() = TokenPosition(0, 0)
}
