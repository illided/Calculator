package com.example.calculator.code

class Calculator {
    fun computeFromString(expression: String) : String {
        try {
            val result = Algorithm(Parser.parseToTokens(expression)).evaluate()
            return "Result:\n$result"
        } catch (e: IllegalArgumentException) {
            return "Error while parsing:\n" + e.message
        } catch (e: ArithmeticException) {
            return "Error while computing:\n" + e.message
        }
    }
}