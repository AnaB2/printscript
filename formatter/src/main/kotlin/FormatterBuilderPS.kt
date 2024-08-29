class FormatterBuilderPS : FormatterBuilder {
    override fun build(rulesPath: String): Formatter {
        return FormatterPS();
    }
}