import ast.ASTNode
import ast.AssignationNode
import ast.BinaryNode
import ast.BlockNode
import ast.ConditionalNode
import ast.DeclarationNode
import ast.FunctionNode
import ast.LiteralNode
import formatOperations.FormatAssignation
import formatOperations.FormatBinary
import formatOperations.FormatBlock
import formatOperations.FormatConditional
import formatOperations.FormatDeclaration
import formatOperations.FormatFunction
import formatOperations.FormatLiteral
import formatOperations.FormatPrint
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import token.Token
import token.TokenPosition
import token.TokenType
import kotlin.test.Test
import org.example.Lexer
import org.example.TokenMapper

class FormatterTest {

    private val yamlPath = "../../kotlin/rules/rules.yaml"
    private val lexer = Lexer(TokenMapper("1.0"))
    private val parser = Parser()

    private val formatAssignation = FormatAssignation()
    private val formatBinary = FormatBinary()
    private val formatBlock = FormatBlock()
    private val formatConditional = FormatConditional()
    private val formatDeclaration = FormatDeclaration()
    private val formatFunction = FormatFunction()
    private val formatLiteral = FormatLiteral()
    private val formatPrint = FormatPrint()

    private val formatter = FormatterBuilderPS().build(
        "src/test/resources/rules.json",
        listOf(
            formatAssignation,
            formatBinary,
            formatBlock,
            formatConditional,
            formatDeclaration,
            formatFunction,
            formatLiteral,
            formatPrint
        )
    )

    @Test
    fun `test formatter with simple expression`(){
        val input = "let x : number = 1 + 2;"
        val tokens : List<Token> = lexer.execute(input)
        val ast : List<ASTNode> = parser.execute(tokens)
        val formatted = formatter.format(ast)
        assertEquals("let x : number = 1 + 2;", formatted)
    }

    @Test
    fun `test canHandle should return true for nodes`() {
        val assignationToken = Token(TokenType.ASSIGNATION, "=", TokenPosition(1, 1), TokenPosition(1, 1))
        val assignationNode = AssignationNode("x", LiteralNode("value", TokenType.NUMBERLITERAL, TokenPosition(1, 1)), TokenType.NUMBERLITERAL, TokenPosition(1, 1))
        assertTrue(formatAssignation.canHandle(assignationNode))

        val binaryNode = BinaryNode(
            LiteralNode("value", TokenType.NUMBERLITERAL, TokenPosition(1, 1)),
            LiteralNode("value", TokenType.NUMBERLITERAL, TokenPosition(1, 1)),
            Token(TokenType.OPERATOR, "+", TokenPosition(1, 1), TokenPosition(1, 1)),
            TokenPosition(1, 1)
        )
        assertTrue(formatBinary.canHandle(binaryNode))

        val blockNode = BlockNode(
            listOf(
                LiteralNode("value", TokenType.NUMBERLITERAL, TokenPosition(1, 1)),
                LiteralNode("value", TokenType.NUMBERLITERAL, TokenPosition(1, 1))
            ),
            TokenPosition(1, 1)
        )
        assertTrue(formatBlock.canHandle(blockNode))

        val conditionalNode = ConditionalNode(
            LiteralNode("condition", TokenType.NUMBERLITERAL, TokenPosition(1, 1)),
            LiteralNode("option1", TokenType.NUMBERLITERAL, TokenPosition(1, 1)),
            LiteralNode("option2", TokenType.NUMBERLITERAL, TokenPosition(1, 1)),
            TokenPosition(1, 1)
        )
        assertTrue(formatConditional.canHandle(conditionalNode))

        val declarationNode = DeclarationNode(
            TokenType.NUMBERLITERAL,
            "n",
            TokenType.NUMBERLITERAL,
            LiteralNode("1", TokenType.NUMBERLITERAL, TokenPosition(1, 1)),
            TokenPosition(1, 1)
        )
        assertTrue(formatDeclaration.canHandle(declarationNode))

        val functionNode = FunctionNode(
            TokenType.FUNCTION,
            LiteralNode("value", TokenType.NUMBERLITERAL, TokenPosition(1, 1)),
            TokenPosition(1, 1)
        )
        assertTrue(formatFunction.canHandle(functionNode))

        val literalNode = LiteralNode("value", TokenType.NUMBERLITERAL, TokenPosition(1, 1))
        assertTrue(formatLiteral.canHandle(literalNode))

    }



}