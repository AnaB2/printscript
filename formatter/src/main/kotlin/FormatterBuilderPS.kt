import formatOperations.FormatOperation

class FormatterBuilderPS : FormatterBuilder {
    override fun build(
        rulesPath: String,
        formatOperations: List<FormatOperation>,
    ): Formatter {
        return FormatterPS(rulesPath, formatOperations)
    }
}
