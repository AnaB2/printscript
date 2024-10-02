package interpreter

import ast.ASTNode
import ast.AssignationNode
import ast.BinaryNode
import ast.BlockNode
import ast.ConditionalNode
import ast.DeclarationNode
import ast.FunctionNode
import ast.LiteralNode
import ast.NilNode
import ast.PrintNode
import token.TokenType

class Interpreter(private val printer: Printer) {
    val variables: MutableMap<String, Any?> = mutableMapOf()

    fun execute(node: ASTNode): Any? {
        return when (node) {
            is LiteralNode -> handleLiteral(node)
            is BinaryNode -> handleBinary(node)
            is AssignationNode -> handleAssignment(node)
            is PrintNode -> handlePrint(node)
            is BlockNode -> handleBlock(node)
            is ConditionalNode -> handleConditional(node)
            is DeclarationNode -> handleDeclaration(node)
            is FunctionNode -> handleFunction(node)
            NilNode -> null
            else -> throw RuntimeException("Unknown node type: ${node::class}")
        }
    }

    private fun handleFunction(node: FunctionNode): Any? {
        return when (node.function) {
            TokenType.FUNCTION -> {
                val value = execute(node.expression)
                println(value)
                value
            }
            else -> throw RuntimeException("Unsupported function: ${node.function}")
        }
    }

    private fun handleLiteral(node: LiteralNode): Any? {
        return when (node.type) {
            TokenType.NUMBERLITERAL -> {
                // Intentamos primero convertir a Int, si falla, intentamos como Float o Double
                node.value.toIntOrNull() ?: node.value.toDoubleOrNull()
                    ?: throw RuntimeException("Invalid number literal: ${node.value}")
            }
            TokenType.STRINGLITERAL -> node.value
            TokenType.BOOLEANLITERAL ->
                when (node.value) {
                    "true" -> true
                    "false" -> false
                    else -> throw RuntimeException("Invalid boolean value: ${node.value}")
                }
            TokenType.DATA_TYPE -> node.value
            TokenType.IDENTIFIER ->
                variables[node.value]
                    ?: throw RuntimeException("Undefined variable: ${node.value}")
            else -> throw RuntimeException("Unsupported literal type: ${node.type}")
        }
    }

    private fun handleBinary(node: BinaryNode): Any? {
        val leftValue = execute(node.left) ?: throw RuntimeException("Invalid left operand")
        val rightValue = execute(node.right) ?: throw RuntimeException("Invalid right operand")
        val operator = node.operator.value

        // Manejo de operaciones aritméticas
        return when (operator) {
            "+" -> handleAddition(leftValue, rightValue)
            "-" -> handleSubtraction(leftValue, rightValue)
            "*" -> handleMultiplication(leftValue, rightValue)
            "/" -> handleDivision(leftValue, rightValue)
            ">" -> handleGreaterThan(leftValue, rightValue)
            "<" -> handleLessThan(leftValue, rightValue)
            else -> throw RuntimeException("Unsupported operator: $operator")
        }
    }

    private fun handleAddition(
        leftValue: Any,
        rightValue: Any,
    ): Any? {
        return when {
            leftValue is Int && rightValue is Int -> leftValue + rightValue
            leftValue is String && rightValue is String -> leftValue + rightValue
            leftValue is Int && rightValue is String -> leftValue.toString() + rightValue
            leftValue is String && rightValue is Int -> leftValue + rightValue.toString() // Modificación aquí
            leftValue is Float && rightValue is Float -> leftValue + rightValue
            leftValue is Double && rightValue is Double -> leftValue + rightValue
            leftValue is Int && rightValue is Float -> leftValue.toFloat() + rightValue
            leftValue is Float && rightValue is Int -> leftValue + rightValue.toFloat()
            leftValue is Int && rightValue is Double -> leftValue.toDouble() + rightValue
            leftValue is Double && rightValue is Int -> leftValue + rightValue.toDouble()
            else -> throw RuntimeException("Unsupported operands for +")
        }
    }

    private fun handleSubtraction(
        leftValue: Any,
        rightValue: Any,
    ): Any? {
        return when {
            leftValue is Int && rightValue is Int -> leftValue - rightValue
            leftValue is Float && rightValue is Float -> leftValue - rightValue
            leftValue is Double && rightValue is Double -> leftValue - rightValue
            leftValue is Int && rightValue is Float -> leftValue.toFloat() - rightValue
            leftValue is Float && rightValue is Int -> leftValue - rightValue.toFloat()
            leftValue is Int && rightValue is Double -> leftValue.toDouble() - rightValue
            leftValue is Double && rightValue is Int -> leftValue - rightValue.toDouble()
            else -> throw RuntimeException("Unsupported operands for -")
        }
    }

    private fun handleMultiplication(
        leftValue: Any,
        rightValue: Any,
    ): Any? {
        if (leftValue is String || rightValue is String) {
            throw RuntimeException("Invalid operation: cannot multiply a string by a number")
        }
        return when {
            leftValue is Int && rightValue is Int -> leftValue * rightValue
            leftValue is Float && rightValue is Float -> leftValue * rightValue
            leftValue is Double && rightValue is Double -> leftValue * rightValue
            leftValue is Int && rightValue is Float -> leftValue.toFloat() * rightValue
            leftValue is Float && rightValue is Int -> leftValue * rightValue.toFloat()
            leftValue is Int && rightValue is Double -> leftValue.toDouble() * rightValue
            leftValue is Double && rightValue is Int -> leftValue * rightValue.toDouble()
            else -> throw RuntimeException("Unsupported operands for *")
        }
    }

    private fun handleDivision(
        leftValue: Any,
        rightValue: Any,
    ): Any? {
        if (leftValue is String || rightValue is String) {
            throw RuntimeException("Invalid operation: cannot divide a string by a number")
        }
        return when {
            leftValue is Int && rightValue is Int -> {
                if (rightValue == 0) throw RuntimeException("Division by zero")
                leftValue / rightValue
            }
            leftValue is Float && rightValue is Float -> {
                if (rightValue == 0f) throw RuntimeException("Division by zero")
                leftValue / rightValue
            }
            leftValue is Double && rightValue is Double -> {
                if (rightValue == 0.0) throw RuntimeException("Division by zero")
                leftValue / rightValue
            }
            leftValue is Int && rightValue is Float -> {
                if (rightValue == 0f) throw RuntimeException("Division by zero")
                leftValue.toFloat() / rightValue
            }
            leftValue is Float && rightValue is Int -> {
                if (rightValue == 0) throw RuntimeException("Division by zero")
                leftValue / rightValue.toFloat()
            }
            leftValue is Int && rightValue is Double -> {
                if (rightValue == 0.0) throw RuntimeException("Division by zero")
                leftValue.toDouble() / rightValue
            }
            leftValue is Double && rightValue is Int -> {
                if (rightValue == 0) throw RuntimeException("Division by zero")
                leftValue / rightValue.toDouble()
            }
            else -> throw RuntimeException("Unsupported operands for /")
        }
    }

    private fun handleGreaterThan(
        leftValue: Any,
        rightValue: Any,
    ): Any? {
        return if (leftValue is Int && rightValue is Int) {
            leftValue > rightValue
        } else {
            throw RuntimeException("Unsupported operands for >")
        }
    }

    private fun handleLessThan(
        leftValue: Any,
        rightValue: Any,
    ): Any? {
        return if (leftValue is Int && rightValue is Int) {
            leftValue < rightValue
        } else {
            throw RuntimeException("Unsupported operands for <")
        }
    }

    private fun handleAssignment(node: AssignationNode): Any? {
        // Evalúa la expresión que se asigna a la variable solo una vez
        val value = execute(node.expression) ?: throw RuntimeException("Invalid assignment in Assignment")

        // Depuración: Imprimir antes de asignar la variable
        println("Asignando a la variable '${node.id}' el valor $value")

        // Verifica si la variable ya está declarada y su tipo
        if (variables.containsKey(node.id)) {
            val expectedType =
                when (variables[node.id]) {
                    is Int -> TokenType.NUMBERLITERAL
                    is String -> TokenType.STRINGLITERAL
                    else -> throw RuntimeException("Unknown type for variable ${node.id}")
                }

            if ((expectedType == TokenType.NUMBERLITERAL && value !is Int) ||
                (expectedType == TokenType.STRINGLITERAL && value !is String)
            ) {
                throw RuntimeException("Invalid expression for type ${expectedType.name.lowercase()}")
            }
        }

        // Actualiza el valor de la variable en el mapa
        variables[node.id] = value

        // Depuración: Imprimir después de asignar
        println("Valor asignado a '${node.id}' es ahora ${variables[node.id]}")

        // Retorna el valor evaluado, en lugar de volver a ejecutar la expresión
        return value
    }

    private fun handleDeclaration(node: DeclarationNode): Any? {
        // Verifica si la variable ya fue declarada
        if (variables.containsKey(node.id)) {
            throw RuntimeException("La variable '${node.id}' ya ha sido declarada")
        }

        // Ejecutar la expresión, si no es NilNode
        val value =
            if (node.expr is NilNode) {
                // Retorna Unit en lugar de null cuando es NilNode
                Unit
            } else {
                execute(node.expr) ?: throw RuntimeException("Expresión inválida en la declaración")
            }

        // Solo guardar la variable si el valor no es Unit
        if (value != Unit) {
            variables[node.id] = value
        }

        return value
    }

    private fun handlePrint(node: PrintNode) {
        val value = execute(node.expression) ?: throw RuntimeException("Invalid expression in PrintNode")
        this.printer.print(value.toString())
    }

    private fun handleBlock(node: BlockNode): Any? {
        var result: Any? = null
        for (blockNode in node.nodes) {
            result = execute(blockNode)
        }
        return result
    }

    private fun handleConditional(node: ConditionalNode): Any? {
        val condition =
            execute(node.condition) as? Boolean
                ?: throw RuntimeException("Condition must evaluate to a boolean")
        return if (condition) execute(node.thenBlock) else execute(node.elseBlock)
    }

    /*
    private fun handleConditional(node: ConditionalNode): Any? {
        // Evalúa la condición
        val condition = evaluate(node.condition)

        // Verifica que la condición se evalúe como un valor booleano
        val conditionValue =
            when (condition) {
                is Boolean -> condition
                else -> throw RuntimeException("Condition must evaluate to a boolean, but got: $condition")
            }

        // Si la condición es verdadera, ejecuta el bloque "then"
        // Si es falsa, ejecuta el bloque "else" si existe
        return if (conditionValue) {
            evaluate(node.thenBlock)
        } else {
            node.elseBlock?.let { evaluate(it) } // Solo evalúa el bloque "else" si existe
        }
    }
     */
}
