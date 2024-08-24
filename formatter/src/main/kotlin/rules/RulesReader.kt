package rules

import org.gradle.internal.impldep.org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream

class RulesReader {
    fun readFile(path: String) : Map<String, Any>{
        val yaml = File(path).readText(); // transforma el contenido a String
        return Yaml().load(yaml) // convierte contenido a Map<String, Any>
    }
}