package linter

enum class LinterVersion(val version: String) {
    VERSION_1_0("1.0"),
    VERSION_1_1("1.1"), // Agrega más versiones según sea necesario
    ;

    companion object {
        fun fromString(version: String): LinterVersion? {
            return values().find { it.version == version }
        }
    }
}
// Changed LinterVersion so it can work with de console input, so i
// write 1.0 it goes to LinterVersion_1_0 and so on
