import ast.AssignationNode
import ast.BinaryNode
import ast.BlockNode
import ast.ConditionalNode
import ast.DeclarationNode
import ast.FunctionNode
import ast.LiteralNode
import ast.PrintNode
import interpreter.Interpreter
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import token.Token
import token.TokenPosition
import token.TokenType
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class InterpreterTests {
    private val position = TokenPosition(1, 1)

    @Test
    fun `test number literal evaluation`() {
        val node = LiteralNode("42", TokenType.NUMBERLITERAL, position)
        val interpreter = Interpreter()
        val result = interpreter.execute(node)
        assertEquals(42, result)
    }

    @Test
    fun `test string literal evaluation`() {
        val node = LiteralNode("Hello, world!", TokenType.STRINGLITERAL, position)
        val interpreter = Interpreter()
        val result = interpreter.execute(node)
        assertEquals("Hello, world!", result)
    }

    @Test
    fun `test boolean literal evaluation`() {
        val trueNode = LiteralNode("true", TokenType.BOOLEAN, position)
        val falseNode = LiteralNode("false", TokenType.BOOLEAN, position)
        val interpreter = Interpreter()
        assertEquals(true, interpreter.execute(trueNode))
        assertEquals(false, interpreter.execute(falseNode))
    }

    @Test
    fun `test addition of integers`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "+", position, position) // Create a token for "+"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        val result = interpreter.execute(node)
        assertEquals(15, result)
    }

    @Test
    fun `test addition of integer and text`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.STRINGLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "+", position, position) // Create a token for "+"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        val result = interpreter.execute(node)
        assertEquals("105", result)
    }

    @Test
    fun `test addition of integer and text2`() {
        val left = LiteralNode("10", TokenType.STRINGLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "+", position, position) // Create a token for "+"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        val result = interpreter.execute(node)
        assertEquals("105", result)
    }

    @Test
    fun `test string concatenation`() {
        val left = LiteralNode("Hello", TokenType.STRINGLITERAL, position)
        val right = LiteralNode("World", TokenType.STRINGLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "+", position, position) // Create a token for "+"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        val result = interpreter.execute(node)
        assertEquals("HelloWorld", result)
    }

    @Test
    fun `test subtraction of integers`() {
        val left = LiteralNode("15", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "-", position, position) // Create a token for "-"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        val result = interpreter.execute(node)
        assertEquals(10, result)
    }

    @Test
    fun `test multiplication of integers`() {
        val left = LiteralNode("3", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("4", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "*", position, position) // Create a token for "*"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        val result = interpreter.execute(node)
        assertEquals(12, result)
    }

    @Test
    fun `test division of integers`() {
        val left = LiteralNode("20", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("4", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "/", position, position) // Create a token for "/"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        val result = interpreter.execute(node)
        assertEquals(5, result)
    }

    @Test
    fun `test division by zero`() {
        val left = LiteralNode("20", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("0", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "/", position, position) // Create a token for "/"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()
        assertThrows(RuntimeException::class.java) {
            interpreter.execute(node)
        }
    }

    @Test
    fun `test variable assignment`() {
        val expression = LiteralNode("42", TokenType.NUMBERLITERAL, position)
        val node = AssignationNode("x", expression, TokenType.ASSIGNATION, position)
        val interpreter = Interpreter()
        interpreter.execute(node)
        assertEquals(42, interpreter.variables["x"])
    }

    @Test
    fun `test print node`() {
        val expression = LiteralNode("Hello, world!", TokenType.STRINGLITERAL, position)
        val node = PrintNode(expression, position)
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        interpreter.execute(node)
        assertEquals("Hello, world!", outputStream.toString().trim())
    }

    @Test
    fun `test block execution`() {
        val expr1 = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val expr2 = LiteralNode("20", TokenType.NUMBERLITERAL, position)
        val assignment = AssignationNode("y", expr2, TokenType.ASSIGNATION, position)
        val print = PrintNode(expr1, position)
        val block = BlockNode(listOf(assignment, print), position)
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        interpreter.execute(block)
        assertEquals("10", outputStream.toString().trim())
    }

    @Test
    fun `test conditional node true`() {
        val condition = LiteralNode("true", TokenType.BOOLEAN, position)
        val thenBlock = PrintNode(LiteralNode("Condition is true", TokenType.STRINGLITERAL, position), position)
        val elseBlock = PrintNode(LiteralNode("Condition is false", TokenType.STRINGLITERAL, position), position)
        val node = ConditionalNode(condition, thenBlock, elseBlock, position)
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        interpreter.execute(node)
        assertEquals("Condition is true", outputStream.toString().trim())
    }

    @Test
    fun `test conditional node false`() {
        val condition = LiteralNode("false", TokenType.BOOLEAN, position)
        val thenBlock = PrintNode(LiteralNode("Condition is true", TokenType.STRINGLITERAL, position), position)
        val elseBlock = PrintNode(LiteralNode("Condition is false", TokenType.STRINGLITERAL, position), position)
        val node = ConditionalNode(condition, thenBlock, elseBlock, position)
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        interpreter.execute(node)
        assertEquals("Condition is false", outputStream.toString().trim())
    }

    @Test
    fun `test undefined variable exception`() {
        val node = LiteralNode("undefinedVariable", TokenType.IDENTIFIER, position)
        val interpreter = Interpreter()

        assertThrows(RuntimeException::class.java) {
            interpreter.execute(node)
        }
    }

    @Test
    fun `test greater than operator`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, ">", position, position) // Create a token for ">"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        val result = interpreter.execute(node)

        // Assuming the result is a Boolean, we expect "10 > 5" to be true
        assertEquals(true, result)
    }

    @Test
    fun `test unsupported operator exception`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "^", position, position) // Use an unsupported operator
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        assertThrows(RuntimeException::class.java) {
            interpreter.execute(node)
        }
    }

    @Test
    fun `test less than operator`() {
        val left = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "<", position, position) // Create a token for "<"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        val result = interpreter.execute(node)

        // Assuming the result is a Boolean, we expect "5 < 10" to be true
        assertEquals(true, result)
    }

    @Test
    fun `test supported operator exception`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("5", TokenType.NUMBERLITERAL, position)
        val operatorToken = Token(TokenType.OPERATOR, "+", position, position) // Use a supported operator
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        assertDoesNotThrow {
            interpreter.execute(node)
        }
    }

    @Test
    fun `test greater than exception`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("true", TokenType.BOOLEAN, position)
        val operatorToken = Token(TokenType.OPERATOR, ">", position, position) // Create a token for ">"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        assertThrows(RuntimeException::class.java) {
            interpreter.execute(node)
        }
    }

    @Test
    fun `test less than exception`() {
        val left = LiteralNode("10", TokenType.NUMBERLITERAL, position)
        val right = LiteralNode("true", TokenType.BOOLEAN, position)
        val operatorToken = Token(TokenType.OPERATOR, "<", position, position) // Create a token for "<"
        val node = BinaryNode(left, right, operatorToken, position)
        val interpreter = Interpreter()

        assertThrows(RuntimeException::class.java) {
            interpreter.execute(node)
        }
    }

    @Test
    fun `test function node`() {
        val expression = LiteralNode("Hello, world!", TokenType.STRINGLITERAL, position)
        val node = FunctionNode(TokenType.FUNCTION, expression, position)
        val interpreter = Interpreter()

        // Redirect output stream to capture print statements
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        interpreter.execute(node)
        assertEquals("Hello, world!", outputStream.toString().trim())
    }

    @Test
    fun `test declaration node`() {
        val expression = LiteralNode("42", TokenType.NUMBERLITERAL, position)
        val node = DeclarationNode(TokenType.KEYWORD, "let", "x", TokenType.DATA_TYPE, expression, position)
        val interpreter = Interpreter()
        interpreter.execute(node)
        assertEquals(42, interpreter.variables["x"])
    }

    @Test
    fun `test programmatic script execution`() {
        val interpreter = Interpreter()

        // Asignación
        val assignNode =
            AssignationNode("x", LiteralNode("10", TokenType.NUMBERLITERAL, position), TokenType.ASSIGNATION, position)
        interpreter.execute(assignNode)

        // Suma
        val additionNode =
            BinaryNode(
                LiteralNode("x", TokenType.IDENTIFIER, position),
                LiteralNode("5", TokenType.NUMBERLITERAL, position),
                Token(TokenType.OPERATOR, "+", position, position),
                position,
            )
        val result = interpreter.execute(additionNode)

        // Verificar
        assertEquals(15, result)
        assertEquals(10, interpreter.variables["x"]) // Verificar que 'x' sigue siendo 10
    }

    @Test
    fun `test programmatic block execution`() {
        val interpreter = Interpreter()

        val expr1 =
            AssignationNode("a", LiteralNode("5", TokenType.NUMBERLITERAL, position), TokenType.ASSIGNATION, position)
        val expr2 =
            AssignationNode("b", LiteralNode("10", TokenType.NUMBERLITERAL, position), TokenType.ASSIGNATION, position)
        val sumNode =
            BinaryNode(
                LiteralNode("a", TokenType.IDENTIFIER, position),
                LiteralNode("b", TokenType.IDENTIFIER, position),
                Token(TokenType.OPERATOR, "+", position, position),
                position,
            )

        val block = BlockNode(listOf(expr1, expr2, sumNode), position)
        val result = interpreter.execute(block)

        assertEquals(15, result)
    }

    @Test
    fun `test programmatic function execution`() {
        val interpreter = Interpreter()

        // Definir una función ficticia
        val functionBody =
            BinaryNode(
                LiteralNode("x", TokenType.IDENTIFIER, position),
                LiteralNode("5", TokenType.NUMBERLITERAL, position),
                Token(TokenType.OPERATOR, "+", position, position),
                position,
            )
        val functionNode = FunctionNode(TokenType.FUNCTION, functionBody, position)

        // Asignar un valor a x
        interpreter.variables["x"] = 10

        // Ejecutar la función
        val result = interpreter.execute(functionNode)

        // Verificar
        assertEquals(15, result) // 10 + 5
    }

    @Test
    fun `test programmatic conditional flow`() {
        val interpreter = Interpreter()

        // Definir una condición que siempre se cumple
        val condition = LiteralNode("true", TokenType.BOOLEAN, position)
        val thenBlock =
            AssignationNode("x", LiteralNode("10", TokenType.NUMBERLITERAL, position), TokenType.ASSIGNATION, position)
        val elseBlock =
            AssignationNode("x", LiteralNode("20", TokenType.NUMBERLITERAL, position), TokenType.ASSIGNATION, position)
        val conditionalNode = ConditionalNode(condition, thenBlock, elseBlock, position)
        assertEquals(10, interpreter.execute(conditionalNode))
    }

    @Test
    fun `test programmatic script with multiple statements`() {
        val interpreter = Interpreter()

        // Declarar varias variables
        interpreter.execute(
            AssignationNode(
                "x",
                LiteralNode("10", TokenType.NUMBERLITERAL, position),
                TokenType.ASSIGNATION,
                position,
            ),
        )
        interpreter.execute(
            AssignationNode(
                "y",
                LiteralNode("20", TokenType.NUMBERLITERAL, position),
                TokenType.ASSIGNATION,
                position,
            ),
        )

        // Sumar y asignar a otra variable
        val sumNode =
            BinaryNode(
                LiteralNode("x", TokenType.IDENTIFIER, position),
                LiteralNode("y", TokenType.IDENTIFIER, position),
                Token(TokenType.OPERATOR, "+", position, position),
                position,
            )
        interpreter.execute(AssignationNode("z", sumNode, TokenType.ASSIGNATION, position))

        // Verificar que las variables están correctamente asignadas
        assertEquals(10, interpreter.variables["x"])
        assertEquals(20, interpreter.variables["y"])
        assertEquals(30, interpreter.variables["z"])
    }
}
