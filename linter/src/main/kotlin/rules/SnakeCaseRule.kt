package rules

import BrokenRule
import Rule
import token.Token
import token.TokenType

class SnakeCaseRule(private var errorMessage: String = "The following identifier must be in snake_case") : Rule {
    private val brokenRules = mutableListOf<BrokenRule>()

    override fun applyRule(tokens: List<List<Token>>): List<BrokenRule> {
        for (row in tokens) {
            for (token in row) {
                if (isIdentifierType(token)) {
                    if (!isSnakeCase(token)) brokenRules.add(BrokenRule(errorMessage, token.getPosition()))
                }
            }
        }
        return brokenRules
    }

    private fun isSnakeCase(token: Token): Boolean {
        val tokenString = token.getValue()
        for (i in 1 until tokenString.length - 1) {
            val currentChar = tokenString[i]
            if (currentChar.isUpperCase()) {
                return false
            }
        }
        return true
    }

    private fun isIdentifierType(token: Token) = token.getType() == TokenType.IDENTIFIER

    override fun getRuleName(): String {
        return "SnakeCase"
    }

    override fun getRuleDescription(): String {
        return errorMessage
    }
}