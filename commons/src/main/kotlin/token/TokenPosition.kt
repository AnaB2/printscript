package org.example.token

// The data class automatically generates equals, hashCode, and toString methods based on the properties
data class TokenPosition(private var row: Int, private var column: Int) {
    fun getRow(): Int {
        return row
    }

    fun getColumn(): Int {
        return column
    }
}
