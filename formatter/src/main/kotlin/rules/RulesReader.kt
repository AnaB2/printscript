package rules

import org.gradle.internal.impldep.org.yaml.snakeyaml.Yaml
import java.io.File
import kotlin.reflect.KClass

class RulesReader(
    private val requiredRules: Map<String, KClass<*>> // reglas requeridas en archivo, ej. para parser 1.0. -> listOf("spaceBeforeColon", "spaceAfterColon", "spaceAroundEquals", "lineFeed")
) {

    fun readFile(path: String) : Map<String, Any> {
        val yaml: String = File(path).readText(); // transforma el contenido a String
        val rulesMap: Map<String, Any> = Yaml().load(yaml); // convierte contenido a Map<String, Any>

        // chequea que se encuentren las configuraciones requeridas
        checkRules(rulesMap, requiredRules);

        return rulesMap;
    }

    // chequea que las reglas requeridas se encuentren en el archivo, y sean del tipo esperado
    private fun checkRules(rulesMap: Map<String, Any>, requiredRules: Map<String, KClass<*>>){
        for ((keyRequired, valueRequired) in requiredRules){
            if(!rulesMap.containsKey(keyRequired)) error("No se encuentra la regla $keyRequired en el archivo");
            if(rulesMap[keyRequired]==null || !valueRequired.isInstance(rulesMap[keyRequired])) error("El valor de la regla $keyRequired no es del tipo esperado");
        }
    }
}