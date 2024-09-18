package formatOperations

import ast.ASTNode
import ast.DeclarationNode
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
        val declKeywordValue =
            if (allowedDeclarationKeyword(
                    declarationNode.declValue,
                )
            ) {
                declarationNode.declValue
            } else {
                throw UnsupportedOperationException(
                    "Unsupported declaration type ${declarationNode.declValue}",
                ) // let or const
            }
        val id = declarationNode.id

        val formatOperationsList = listOf(FormatLiteral(), FormatBinary())
        val exprValue =
            formatOperationsList.find { it -> it.canHandle(declarationNode.expr) }
                ?.format(declarationNode.expr, formatter)

        val valType = declarationNode.dataTypeValue // boolean, string or number

        // handle spaces
        val spaceBeforeColon = formatter.getRules()["spaceBeforeColon"] as Boolean
        val spaceAfterColon = formatter.getRules()["spaceAfterColon"] as Boolean
        val spaceAroundEquals = formatter.getRules()["spaceAroundEquals"] as Boolean

        val equal = handleSpace.handleSpace("=", spaceAroundEquals, spaceAroundEquals)
        val colon = handleSpace.handleSpace(":", spaceBeforeColon, spaceAfterColon)

        return "$declKeywordValue $id$colon$valType$equal$exprValue"
    }

    private fun allowedDeclarationKeyword(declKeyword: String): Boolean {
        return allowedDeclarationKeywords.contains(
            declKeyword,
        )
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
