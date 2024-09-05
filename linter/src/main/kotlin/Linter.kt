import ast.ASTNode
import rules.Rule
import rules.RuleFactory
import rules.RuleValidator

class Linter(private var version: LinterVersion) {
    private var rules: List<Rule> = listOf()
    private val jsonReader = RuleJsonReader()
    private val ruleFactory = RuleFactory()
    private val tokenizer = Tokenizer()
    private val validator = RuleValidator()
    private val fileManager = OutputFileManager()

    fun getVersion(): LinterVersion {
        return version
    }

    fun readJson(rulesPath: String) {
        val jsonRules = jsonReader.getRulesFromJson(rulesPath)
        rules = ruleFactory.createRules(jsonRules, version)
    }

    fun check(trees: List<ASTNode>): LinterOutput {
        val tokens = tokenizer.parseToTokens(trees)
        val brokenRules = validator.checkRule(rules, tokens)
        val scaOutput = LinterOutput()
        if (brokenRules.isNotEmpty()) {
            for (brokenRule in brokenRules) {
                scaOutput.addBrokenRule(brokenRule)
            }
        }
        return scaOutput
    }

    fun getRules(): List<Rule> {
        return rules
    }

    fun writeToFile(content: String, filePath: String) {
        fileManager.saveToFile(content, filePath)
    }

    fun createTxtContent(brokenRules: List<BrokenRule>): String {
        return fileManager.createTxtReport(brokenRules)
    }

    fun createHtmlContent(brokenRules: List<BrokenRule>): String {
        return fileManager.createHtmlReport(brokenRules)
    }
}
