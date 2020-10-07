package com.example.calculator.code

//To add your operator create it and add it to the list, then modify the regex in the parser
val NON_NUMBER_TOKENS = setOf<Token>(OpenBracket, ClosedBracket, Minus, Plus, Multiply, Divide)

object OpenBracket: Token {
    override val stringPresentation: String = "("
}

object ClosedBracket: Token {
    override val stringPresentation: String = ")"
}

object Minus : UnaryOperatorToken, BinaryOperatorToken {
    override val priority: Int = 1
    override val stringPresentation: String = "-"
    override fun evaluate(firstNumber: NumberToken): NumberToken =
        createNumberToken(-1 * firstNumber.value)

    override fun evaluate(firstNumber: NumberToken, secondNumber: NumberToken): NumberToken =
        createNumberToken(firstNumber.value - secondNumber.value)
}

object Plus : UnaryOperatorToken, BinaryOperatorToken {
    override val priority: Int = 1
    override val stringPresentation: String = "+"
    override fun evaluate(firstNumber: NumberToken): NumberToken =
        createNumberToken(firstNumber.value)

    override fun evaluate(firstNumber: NumberToken, secondNumber: NumberToken): NumberToken =
        createNumberToken(firstNumber.value + secondNumber.value)
}

object Multiply : BinaryOperatorToken {
    override val priority: Int = 2
    override val stringPresentation: String = "*"
    override fun evaluate(firstNumber: NumberToken, secondNumber: NumberToken): NumberToken =
        createNumberToken(firstNumber.value * secondNumber.value)
}

object Divide : BinaryOperatorToken {
    override val priority: Int = 2
    override val stringPresentation: String = "/"
    override fun evaluate(firstNumber: NumberToken, secondNumber: NumberToken): NumberToken =
        createNumberToken(firstNumber.value / secondNumber.value)
}
