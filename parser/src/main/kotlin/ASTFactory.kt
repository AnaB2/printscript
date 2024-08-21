import ast.AstNode
import token.Token

interface ASTFactory {
    fun createAST(tokens: List<Token>): AstNode

    fun canHandle(tokens: List<Token>): Boolean
}