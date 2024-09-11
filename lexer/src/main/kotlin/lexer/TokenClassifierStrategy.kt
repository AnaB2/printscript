package org.example.lexer

interface TokenClassifierStrategy {
    fun classify(tokenValue: String): Boolean
}
