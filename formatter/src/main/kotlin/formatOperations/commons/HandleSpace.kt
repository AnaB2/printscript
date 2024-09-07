package formatOperations.commons

class HandleSpace {
    fun handleSpace(
        tokenValue: String,
        spaceBefore: Boolean,
        spaceAfter: Boolean,
    ): String {
        var result = tokenValue.trim() // elimina los espacios al inicio y al final
        if (spaceBefore) {
            result = " " + result
        }
        if (spaceAfter) {
            result += " "
        }
        return result
    }
}
