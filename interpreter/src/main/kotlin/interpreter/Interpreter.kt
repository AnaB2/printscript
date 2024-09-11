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

    fun evaluate(node: ASTNode): Any? {
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
                val value = evaluate(node.expression)
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
        val leftValue = evaluate(node.left) ?: throw RuntimeException("Invalid left operand")
        val rightValue = evaluate(node.right) ?: throw RuntimeException("Invalid right operand")

        val operator = node.operator.value // This should be the actual operator symbol like "+", "-", etc.

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
            else -> throw RuntimeException("Unsupported operands for +")
        }
    }

    private fun handleSubtraction(
        leftValue: Any,
        rightValue: Any,
    ): Any? {
        return if (leftValue is Int && rightValue is Int) {
            leftValue - rightValue
        } else {
            throw RuntimeException("Unsupported operands for -")
        }
    }

    private fun handleMultiplication(
        leftValue: Any,
        rightValue: Any,
    ): Any? {
        return if (leftValue is Int && rightValue is Int) {
            leftValue * rightValue
        } else if (leftValue is Float && rightValue is Float) {
            leftValue * rightValue
        } else if (leftValue is Double && rightValue is Double) {
            leftValue * rightValue
        } else {
            throw RuntimeException("Unsupported operands for multiplication: $leftValue, $rightValue")
        }
    }

    private fun handleDivision(
        leftValue: Any,
        rightValue: Any,
    ): Any? {
        return if (leftValue is Int && rightValue is Int) {
            if (rightValue == 0) throw RuntimeException("Division by zero")
            leftValue / rightValue
        } else {
            throw RuntimeException("Unsupported operands for /")
        }
    }

    private fun handleAssignment(node: AssignationNode): Any? {
        val value = evaluate(node.expression) ?: throw RuntimeException("Invalid assignment in Assignment")
        variables[node.id] = value
        return value
    }

    private fun handleDeclaration(node: DeclarationNode): Any? {
        val value = evaluate(node.expr) ?: throw RuntimeException("Invalid assignment in Declaration")
        variables[node.id] = value
        return value
    }

    private fun handlePrint(node: PrintNode): Any? {
        val value = evaluate(node.expression)
        println(value)
        return value
    }

    private fun handleBlock(node: BlockNode): Any? {
        var result: Any? = null
        for (blockNode in node.nodes) {
            result = evaluate(blockNode)
        }
        return result
    }

    private fun handleConditional(node: ConditionalNode): Any? {
        val condition =
            evaluate(node.condition) as? Boolean
                ?: throw RuntimeException("Condition must evaluate to a boolean")
        return if (condition) evaluate(node.thenBlock) else evaluate(node.elseBlock)
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
