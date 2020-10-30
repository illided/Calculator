package com.example.calculator.code

interface Token {
    val stringPresentation: String
}

data class NumberToken(override val stringPresentation: String, val value: Double) : Token

fun createNumberToken(stringPresentation: String): NumberToken =
    NumberToken(stringPresentation, stringPresentation.toDouble())

fun createNumberToken(value: Double): NumberToken =
    NumberToken(value.toString(), value)

interface BinaryInfixOperatorToken : Token {
    val priority: Int
    fun evaluate(firstNumber: NumberToken, secondNumber: NumberToken): NumberToken
}

interface UnaryOperatorToken : Token {
    fun evaluate(firstNumber: NumberToken): NumberToken
}
