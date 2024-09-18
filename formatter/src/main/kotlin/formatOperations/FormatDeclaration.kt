package formatOperations

import ast.ASTNode
import ast.DeclarationNode
import ast.LiteralNode
import formatOperations.commons.HandleSpace
import formatter.Formatter
import token.TokenType

class FormatDeclaration(
    private val allowedDeclarationKeywords: List<String>,
    private val allowedValueTypes: Map<TokenType, String>,
) : FormatOperation {
    private val handleSpace: HandleSpace = HandleSpace()

    override fun canHandle(astNode: ASTNode): Boolean {
        return astNode is DeclarationNode
    }

    override fun format(
        node: ASTNode,
        formatter: Formatter,
    ): String {
        if (!canHandle(node)) error("Node isn't a DeclarationNode")
        val declarationNode = node as DeclarationNode
        val declKeywordValue = allowedDeclarationKeyword(declarationNode.declKeyword) // let or const
        val id = declarationNode.id
        val exprNode = declarationNode.expr as LiteralNode
        val valType = defineValueType(exprNode.type) // boolean, string or number

        // handle spaces
        val spaceBeforeColon = formatter.getRules()["spaceBeforeColon"] as Boolean
        val spaceAfterColon = formatter.getRules()["spaceAfterColon"] as Boolean
        val spaceAroundEquals = formatter.getRules()["spaceAroundEquals"] as Boolean

        val equal = handleSpace.handleSpace("=", spaceAroundEquals, spaceAroundEquals)
        val colon = handleSpace.handleSpace(":", spaceBeforeColon, spaceAfterColon)
        val exprValue = formatter.format(exprNode)

        return "$declKeywordValue $id$colon$valType$equal$exprValue"
    }

    private fun allowedDeclarationKeyword(declKeyword: String): String {
        if (allowedDeclarationKeywords.contains(
                declKeyword,
            )
        ) {
            return declKeyword
        } else {
            throw UnsupportedOperationException("Unsupported declaration type $declKeyword")
        }
    }

    private fun defineValueType(valueType: TokenType): String {
        if (allowedValueTypes.containsKey(
                valueType,
            )
        ) {
            return allowedValueTypes[valueType] as String
        } else {
            throw UnsupportedOperationException("Unsupported value type $valueType")
        }
    }
}
