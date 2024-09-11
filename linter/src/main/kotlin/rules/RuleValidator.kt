package rules

import linter.BrokenRule
import token.Token

class RuleValidator {
    fun checkRule(
        rules: List<Rule>,
        tokens: List<List<Token>>,
    ): List<BrokenRule> {
        val brokenRules = mutableListOf<BrokenRule>()
        for (rule in rules) {
            val violations = rule.applyRule(tokens)
            brokenRules.addAll(violations)
        }
        return brokenRules
    }
}
