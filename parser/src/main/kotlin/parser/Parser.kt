package parser

import ast.ASTNode
import factories.ASTFactory
import factories.AssignationFactory
import factories.ConditionalFactory
import factories.DeclarationFactory
import factories.FunctionFactory
import factories.PrintlnFactory
import token.Token

class Parser {
    private val factories: List<ASTFactory> =
        listOf(
            ConditionalFactory(),
            PrintlnFactory(),
            DeclarationFactory(),
            AssignationFactory(),
            FunctionFactory(),
        )

    fun execute(tokens: List<Token>): List<ASTNode> {
        val result = mutableListOf<ASTNode>()
        val sameLineTokens = getSameLineTokens(tokens)
        for (tokenList in sameLineTokens) {
            val astFactory = determineFactory(tokenList)
            if (astFactory != null) {
                result.add(astFactory.createAST(tokenList))
            } else {
                throw Exception("Can't handle this sentence")
            }
        }
        return result
    }

    private fun determineFactory(tokens: List<Token>): ASTFactory? {
        return factories.find { it.canHandle(tokens) }
    }

    private fun getSameLineTokens(tokenList: List<Token>): List<List<Token>> {
        val rows = mutableListOf<List<Token>>()
        var singleRow = mutableListOf<Token>()
        var llaves = 0
        var expectingElse = false

        for (i in tokenList.indices) {
            val token = tokenList[i]

            // Open a block
            if (token.value == "{") {
                llaves++
                singleRow.add(token)
            } else if (token.value == "}") {
                llaves--
                singleRow.add(token)

                // If we're closing a block and not inside another block
                if (llaves == 0) {
                    // Check if the next token is 'else'
                    if (i + 1 < tokenList.size && tokenList[i + 1].value == "else") {
                        expectingElse = true
                    } else {
                        // Add current row because no 'else' follows
                        rows.add(singleRow)
                        singleRow = mutableListOf()
                        expectingElse = false
                    }
                }
            } else if (token.value == "if") {
                if (singleRow.isNotEmpty()) {
                    rows.add(singleRow)
                }
                singleRow = mutableListOf()
                singleRow.add(token)
                expectingElse = true
            } else if (token.value == "else" && expectingElse) {
                singleRow.add(token)
                expectingElse = false
            } else if ((token.value == ";" || token.value == "\n") && llaves == 0) {
                if (singleRow.isNotEmpty()) {
                    rows.add(singleRow)
                    singleRow = mutableListOf()
                }
            } else {
                singleRow.add(token)
            }
        }

        if (singleRow.isNotEmpty()) {
            rows.add(singleRow)
        }

        if (rows.isEmpty()) {
            throw Exception("Error: No valid code.")
        }

        return rows
    }
}
