package formatOperations.commons

class HandleSemicolon {
    // agrega un punto y coma al final si no hay uno
    fun handleSemicolon(text: String): String {
        val result = text.trimEnd() // elimina espacios del final
        return if (result.endsWith(";")) result else "$result;"
    }
}
