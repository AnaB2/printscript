package org.example

interface TokenClassifierStrategy {
    fun classify(tokenValue: String): Boolean
}
