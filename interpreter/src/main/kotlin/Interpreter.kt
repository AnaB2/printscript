import ast.*
import token.*


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
            is FunctionNode -> throw RuntimeException("Function not implemented")
            NilNode -> null
            else -> throw RuntimeException("Unknown node type: ${node::class}")
        }
    }

    private fun handleLiteral(node: LiteralNode): Any? {
        return when (node.type) {
            TokenType.NUMBERLITERAL -> node.value.toIntOrNull()
                ?: throw RuntimeException("Invalid number literal: ${node.value}")
            TokenType.STRINGLITERAL -> node.value
            TokenType.BOOLEAN -> when (node.value) {
                "true" -> true
                "false" -> false
                else -> throw RuntimeException("Invalid boolean value: ${node.value}")
            }
            TokenType.IDENTIFIER -> variables[node.value]
                ?: throw RuntimeException("Undefined variable: ${node.value}")
            else -> throw RuntimeException("Unsupported literal type: ${node.type}")
        }
    }

    private fun handleBinary(node: BinaryNode): Any? {
        val leftValue = evaluate(node.left) ?: throw RuntimeException("Invalid left operand")
        val rightValue = evaluate(node.right) ?: throw RuntimeException("Invalid right operand")

        val operator = node.operator.getValue() // This should be the actual operator symbol like "+", "-", etc.

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


    private fun handleGreaterThan(leftValue: Any, rightValue: Any): Any? {
        return if (leftValue is Int && rightValue is Int) {
            leftValue > rightValue
        } else {
            throw RuntimeException("Unsupported operands for >")
        }
    }

    private fun handleLessThan(leftValue: Any, rightValue: Any): Any? {
        return if (leftValue is Int && rightValue is Int) {
            leftValue < rightValue
        } else {
            throw RuntimeException("Unsupported operands for <")
        }
    }


    private fun handleAddition(leftValue: Any, rightValue: Any): Any? {
        return when {
            leftValue is Int && rightValue is Int -> leftValue + rightValue
            leftValue is String && rightValue is String -> leftValue + rightValue
            else -> throw RuntimeException("Unsupported operands for +")
        }
    }

    private fun handleSubtraction(leftValue: Any, rightValue: Any): Any? {
        return if (leftValue is Int && rightValue is Int) {
            leftValue - rightValue
        } else {
            throw RuntimeException("Unsupported operands for -")
        }
    }
    private fun handleMultiplication(leftValue: Any, rightValue: Any): Any? {
        return if (leftValue is Int && rightValue is Int) {
            leftValue * rightValue
        } else {
            throw RuntimeException("Unsupported operands for *")
        }
    }
    private fun handleDivision(leftValue: Any, rightValue: Any): Any? {
        return if (leftValue is Int && rightValue is Int) {
            if (rightValue == 0) throw RuntimeException("Division by zero")
            leftValue / rightValue
        } else {
            throw RuntimeException("Unsupported operands for /")
        }
    }
    private fun handleAssignment(node: AssignationNode): Any? {
        val value = evaluate(node.expression) ?: throw RuntimeException("Invalid assignment")
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
        val condition = evaluate(node.condition) as? Boolean
            ?: throw RuntimeException("Condition must evaluate to a boolean")
        return if (condition) evaluate(node.thenBlock) else evaluate(node.elseBlock)
    }
}

