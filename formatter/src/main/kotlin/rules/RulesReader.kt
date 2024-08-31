package rules

import org.gradle.internal.impldep.org.yaml.snakeyaml.Yaml
import java.io.File

class RulesReader(
    private val requiredRules: List<String> // reglas requeridas en archivo, ej. para parser 1.0. -> listOf("spaceBeforeColon", "spaceAfterColon", "spaceAroundEquals", "lineFeed")
) {

    fun readFile(path: String) : Map<String, Any> {
        val yaml: String = File(path).readText(); // transforma el contenido a String
        val rulesMap: Map<String, Any> = Yaml().load(yaml); // convierte contenido a Map<String, Any>

        // chequea que se encuentren las configuraciones requeridas
        val missingRules = requiredRules.filter { !rulesMap.containsKey(it) };
        if(missingRules.isNotEmpty()) throw IllegalArgumentException("No se encuentran las siguientes reglas en el archivo: ${missingRules.joinToString(", ")}");

        return rulesMap;
    }
}