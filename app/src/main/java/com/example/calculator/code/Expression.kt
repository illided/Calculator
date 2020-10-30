package com.example.calculator.code

class Expression(private val expression: String) {
    val value: Double by lazy {
        Algorithm(Parser.parseToTokens(expression)).evaluate()
    }
}