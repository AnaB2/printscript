package rules

import BrokenRule
import Rule
import token.Token
import token.TokenType

class CamelCaseRule(
    private var errorMessage: String = "The following identifier must be in camelCase"
) : Rule {

    private val brokenRules = mutableListOf<BrokenRule>()

    override fun applyRule(tokens: List<List<Token>>): List<BrokenRule> {
        for (row in tokens) {
            for (token in row) {
                if (isIdentifier()(token) && !isCamelCase(token)) {
                    brokenRules.add(BrokenRule(errorMessage, token.getPosition()))
                }
            }
        }
        return brokenRules
    }

    private fun isIdentifier() = { token: Token -> token.getType() == TokenType.IDENTIFIER }

    private fun isCamelCase(token: Token): Boolean {
        val segment = token.getValue().split("_")
        return segment.size <= 1 || segment.all { it[0].isLowerCase() }
    }

    override fun getRuleName(): String {
        return "CamelCase"
    }

    override fun getRuleDescription(): String {
        return errorMessage
    }
}
