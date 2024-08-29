package rules

import BrokenRule
import Rule
import token.Token
import token.TokenType

class EmptyPrintRule(private var errorMessage: String = "Println must not be called with an expression") : Rule {

    private val brokenRules = mutableListOf<BrokenRule>()

    override fun applyRule(tokens: List<List<Token>>): List<BrokenRule> {
        for (row in tokens) {
            if (checkIsPrintln(row) && checkIsExpression(row)) {
                brokenRules.add(BrokenRule(errorMessage, row[0].getPosition()))
            }
        }
        return brokenRules
    }

    private fun checkIsPrintln(tokens : List<Token>): Boolean {
        val firstToken = tokens[0]
        return isPrintln(firstToken)
    }

    private fun isPrintln(token: Token): Boolean {
        return token.getType() == TokenType.FUNCTION && token.getValue().lowercase() == "println"
    }

    private fun checkIsExpression(tokens: List<Token>): Boolean {
        for (token in tokens) {
            if (isExpression(token)) {
                return true
            }
        }
        return false
    }

    private fun isExpression(token: Token): Boolean {
        return token.getType() != TokenType.IDENTIFIER &&
                token.getType() != TokenType.PUNCTUATOR &&
                token.getType() != TokenType.LITERAL
    }

    override fun getRuleName(): String {
        return "EmptyPrint"
    }

    override fun getRuleDescription(): String {
        return errorMessage
    }
}