import formatOperations.FormatOperation

interface FormatterBuilder {
    fun build(
        rulesPath: String,
        formatOperations: List<FormatOperation>,
    ): Formatter
}
