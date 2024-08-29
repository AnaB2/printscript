package rules

import org.gradle.internal.impldep.org.yaml.snakeyaml.Yaml
import java.io.File

class RulesReader {
    fun readFile(path: String) : Map<String, Any> {
        val yaml: String = File(path).readText(); // transforma el contenido a String
        val rulesMap: Map<String, Any> = Yaml().load(yaml); // convierte contenido a Map<String, Any>

        // chequea que se encuentren las configuraciones requeridas
        val requiredRules = listOf("spaceBeforeColon", "spaceAfterColon", "spaceAroundEquals", "lineFeed");
        val missingRules = requiredRules.filter { !rulesMap.containsKey(it) };
        if(missingRules.isNotEmpty()) throw IllegalArgumentException("No se encuentran las siguientes reglas en el archivo: ${missingRules.joinToString(", ")}");

        return rulesMap;
    }
}