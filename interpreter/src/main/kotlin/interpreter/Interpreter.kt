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

class Interpreter {
    val variables = mutableMapOf<String, Any>()
    private val printBuffer = StringBuilder()

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
            TokenType.NUMBERLITERAL ->
                node.value.toIntOrNull()
                    ?: throw RuntimeException("Invalid number literal: ${node.value}")
            TokenType.STRINGLITERAL -> node.value
            TokenType.BOOLEAN ->
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

        val operator = node.operator.value // Esto debería ser el símbolo del operador como "+", "-", etc.

        // Verificar si alguno de los operandos es una cadena y se intenta realizar una operación aritmética
        if (leftValue is String || rightValue is String) {
            when (operator) {
                "*", "/", "+", "-" -> throw RuntimeException("Invalid operation: cannot perform arithmetic with strings")
            }
        }

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

    private fun handleAddition(
        leftValue: Any,
        rightValue: Any,
    ): Any? {
        return when {
            leftValue is Int && rightValue is Int -> leftValue + rightValue
            leftValue is String && rightValue is String -> leftValue + rightValue
            leftValue is Int && rightValue is String -> (leftValue.toString() + rightValue)
            leftValue is String && rightValue is Int -> (leftValue + rightValue.toString())
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
            throw RuntimeException("Invalid operation: cannot multiply a string by a number")
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

    private fun handleAssignment(node: AssignationNode): Any? {
        val value = execute(node.expression) ?: throw RuntimeException("Invalid assignment in Assignment")

        // Verifica si el valor asignado es del tipo correcto
        val expectedType =
            when (val existingValue = variables[node.id]) {
                is Int -> TokenType.NUMBERLITERAL
                is String -> TokenType.STRINGLITERAL
                else -> throw RuntimeException("Unknown type for variable ${node.id}")
            }

        if ((expectedType == TokenType.NUMBERLITERAL && value !is Int) ||
            (expectedType == TokenType.STRINGLITERAL && value !is String)
        ) {
            throw RuntimeException("Invalid expression for type ${expectedType.name.toLowerCase()}")
        }

        variables[node.id] = value
        return value
    }

    private fun handleDeclaration(node: DeclarationNode): Any? {
        val value = execute(node.expr) ?: throw RuntimeException("Invalid assignment in Declaration")

        // Valida tipos antes de asignar
        if (node.declType == TokenType.NUMBERLITERAL && value !is Int) {
            throw RuntimeException("Invalid expression for type number")
        }
        if (node.declType == TokenType.STRINGLITERAL && value !is String) {
            throw RuntimeException("Invalid expression for type string")
        }

        variables[node.id] = value
        return value
    }

    private fun handlePrint(node: PrintNode): Any? {
        val value = execute(node.expression)

        // Accumulate the output in a buffer
        printBuffer.append(value).append("\n")

        // Flush the buffer periodically (e.g., after every print)
        flushOutput()

        return value
    }

    private fun flushOutput() {
        // Write the content to the console (or any output stream)
        print(printBuffer.toString())

        // Clear the buffer after flushing
        printBuffer.clear()
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
