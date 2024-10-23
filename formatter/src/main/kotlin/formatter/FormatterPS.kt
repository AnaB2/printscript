package formatter

import ast.ASTNode
import formatOperations.FormatOperation
import formatOperations.commons.HandleLineBreak
import formatOperations.commons.HandleSemicolon
import lexer.Lexer
import parser.Parser
import rules.RulesReader
import token.Token
import token.TokenType

class FormatterPS : Formatter {
    private val rulesPath: String
    private val formatOperations: List<FormatOperation>
    private val rulesReader: RulesReader
    private val handleSemicolon = HandleSemicolon()
    private val handleLineBreak = HandleLineBreak()
    private val lexer: Lexer
    private val parser: Parser

    constructor(rulesReader: RulesReader, rulesPath: String, formatOperations: List<FormatOperation>, lexer: Lexer, parser: Parser) {
        this.rulesPath = rulesPath
        this.formatOperations = formatOperations
        this.rulesReader = rulesReader
        this.lexer = lexer
        this.parser = parser
    }

    override fun format(input: String): String {
        val tokens: List<Token> = lexer.execute(input)
        val tokensWithSemicolon: List<Token> = addSemicolonForEachStatement(tokens)
        val astNodes: List<ASTNode> = parser.execute(tokensWithSemicolon)

        val formatedNodes: List<String> = astNodes.map { node -> formatNode(node) }

        val formatedNodesWithSemicolon =
            formatedNodes.map {
                    line ->
                if (!line.contains("if")) handleSemicolon.handleSemicolon(line) else line
            }

        val numberOfLineBreak = rulesReader.readFile(rulesPath)["lineBreak"] as Int
        val result = handleLineBreak.handleLineBreak(formatedNodesWithSemicolon, numberOfLineBreak)

        return result
    }

    override fun format(astNode: ASTNode): String {
        return formatNode(astNode)
    }

    private fun formatNode(node: ASTNode): String {
        val formatter = formatOperations.find { it -> it.canHandle(node) }
        return formatter?.format(node, this) ?: ""
    }

    override fun getRules(): Map<String, Any> {
        return rulesReader.readFile(rulesPath)
    }

    private fun addSemicolonForEachStatement(tokens: List<Token>): List<Token> {
        val result: MutableList<Token> = mutableListOf()
        for (token in tokens) {
            if (token.value == "\n") {
                if (result.last().value != ";" && result.last().value != "}") {
                    result.add(
                        Token(
                            TokenType.PUNCTUATOR,
                            ";",
                            token.getPosition(),
                            token.getPosition(),
                        ),
                    )
                }
            } else {
                result.add(token)
            }
        }
        if (result.last().value != "}" || result.last().value != ";") {
            result.add(
                Token(
                    TokenType.PUNCTUATOR,
                    ";",
                    result.last().getPosition(),
                    result.last().getPosition(),
                ),
            )
        }
        return result.toList()
    }
}
