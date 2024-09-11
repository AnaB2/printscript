package org.example.lexer

import token.TokenType

class TokenMapper(private val version: String) {
    private val strategyMap: MutableMap<TokenType, TokenClassifierStrategy> = mutableMapOf()

    init {
        initializeStrategies()
    }

    // Inicializa las estrategias en el mapa según la versión
    private fun initializeStrategies() {
        when (version) {
            "1.0" -> initializeVersion10Strategies()
            "1.1" -> initializeVersion11Strategies()
            else -> throw IllegalArgumentException("Unsupported version: $version")
        }
    }

    // Configura las estrategias para la versión 1.0
    private fun initializeVersion10Strategies() {
        strategyMap[TokenType.KEYWORD] = RegexTokenClassifier("""\blet\b""".toRegex())
        strategyMap[TokenType.FUNCTION] = RegexTokenClassifier("""\bprintln\b""".toRegex())
        strategyMap[TokenType.PARENTHESIS] = RegexTokenClassifier("""\(|\)""".toRegex())
        strategyMap[TokenType.DECLARATOR] = RegexTokenClassifier(""":""".toRegex())
        strategyMap[TokenType.ASSIGNATION] = RegexTokenClassifier("""=""".toRegex())
        strategyMap[TokenType.DATA_TYPE] = RegexTokenClassifier("""\bstring\b|\bnumber\b""".toRegex())
        strategyMap[TokenType.OPERATOR] = RegexTokenClassifier("""[\+\-\*/%=><!&|^~]+""".toRegex())
        strategyMap[TokenType.IDENTIFIER] = RegexTokenClassifier("""\b[a-zA-Z_][a-zA-Z0-9_]*\b""".toRegex())
        strategyMap[TokenType.STRINGLITERAL] = RegexTokenClassifier("\'[^\']*\'|\"[^\"]*\"".toRegex())
        strategyMap[TokenType.NUMBERLITERAL] = RegexTokenClassifier("[0-9]+(\\.[0-9]+)?".toRegex())
        strategyMap[TokenType.PUNCTUATOR] = RegexTokenClassifier("""[()\[\],;.]""".toRegex())
    }

    private fun initializeVersion11Strategies() {
        initializeStrategies()
        strategyMap[TokenType.CONDITIONAL] = RegexTokenClassifier("""\bif\b|\belse\b""".toRegex())
        strategyMap[TokenType.KEYWORD] = RegexTokenClassifier("""\blet\b|\bconst\b""".toRegex())
        strategyMap[TokenType.FUNCTION] = RegexTokenClassifier("println|readInput|readEnv".toRegex())
        strategyMap[TokenType.DATA_TYPE] = RegexTokenClassifier("(string|number|boolean)".toRegex())
        strategyMap[TokenType.BOOLEANLITERAL] = RegexTokenClassifier("(true|false)".toRegex())
        strategyMap[TokenType.PUNCTUATOR] = RegexTokenClassifier("""[{}()\[\],;.]""".toRegex())
    }

    fun classify(input: String): TokenType {
        if (input.isBlank()) {
            return TokenType.UNKNOWN
        }
        for ((type, strategy) in strategyMap) {
            if (strategy.classify(input)) {
                return type
            }
        }
        return TokenType.UNKNOWN
    }

    fun getStrategyMap(): Map<TokenType, TokenClassifierStrategy> {
        return strategyMap
    }
}
