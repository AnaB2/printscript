class LinterOutput {

    private var isCorrect: Boolean = true
    private var brokenRules: MutableList<String> = mutableListOf()

    fun isCorrect(): Boolean {
        return isCorrect
    }

    fun getBrokenRules(): List<String> {
        return brokenRules
    }

    fun addBrokenRule(brokenRule: BrokenRule) {
        isCorrect = false
        val ruleAsString = formatBrokenRule(brokenRule)
        brokenRules.add(ruleAsString)
    }

    private fun formatBrokenRule(brokenRule: BrokenRule): String {
        val position = brokenRule.errorPosition
        return "Broken rule: ${brokenRule.ruleDescription} at ${position.row}:${position.column}"
    }
}
