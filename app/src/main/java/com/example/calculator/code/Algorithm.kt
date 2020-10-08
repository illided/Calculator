package com.example.calculator.code

import java.util.*

//Данный алгоритм поддерживает только инфиксные операторы и унарные операторы
//Если надо добавить операторы с новыми типами поведения, придется писать новый алгоритм
class Algorithm(private val expression: List<Token>) {
    private val stackOfNumberTokens: Stack<NumberToken> = Stack()
    private val stackOfNonNumberTokens: Stack<Token> = Stack()
    private val stackOfIndexes: Stack<Int> = Stack()

    fun evaluate(): Double {
        checkForBinaryUnaryOperatorsMixUsage(expression)

        expression.mapIndexed { index, token ->
            when {
                token is OpenBracket -> stackOfNonNumberTokens.push(OpenBracket)
                token is ClosedBracket -> closedBracketAvalanche()
                isUnary(index) -> {
                    stackOfNonNumberTokens.push(token)
                    stackOfIndexes.push(index)
                }
                isInfix(index) -> {
                    priorityAvalanche((token as BinaryInfixOperatorToken).priority)
                    stackOfNonNumberTokens.push(token)
                    stackOfIndexes.push(index)
                }
                token is NumberToken -> {
                    stackOfNumberTokens.push(token)
                }
                else -> throw ArithmeticException("Token was received but no behaviour associated")
            }
        }

        if (stackOfNonNumberTokens.size != 0 || stackOfNumberTokens.size != 1) {
            throw ArithmeticException("Some part of expression was not calculated")
        } else {
            return stackOfNumberTokens.pop().value
        }
    }

    private fun checkForBinaryUnaryOperatorsMixUsage(expression: List<Token>) {
        for (i in 1..expression.size - 2) {
            if (expression[i] is UnaryOperatorToken
                && expression[i] is BinaryInfixOperatorToken
                && (expression[i - 1] is BinaryInfixOperatorToken
                        || expression[i + 1] is BinaryInfixOperatorToken)
            ) {
                throw ArithmeticException("Wrong usage of binary/unary operator")
            }
        }
    }

    private fun isInfix(index: Int): Boolean {
        return expression[index] is BinaryInfixOperatorToken
                && expression[index - 1] !is OpenBracket
                && expression[index + 1] !is ClosedBracket
    }

    private fun isUnary(index: Int): Boolean {
        return expression[index] is UnaryOperatorToken
                && expression[index - 1] !is ClosedBracket
                && expression[index - 1] !is NumberToken
                && expression[index + 1] !is ClosedBracket
    }


    private fun closedBracketAvalanche() {
        while (stackOfNonNumberTokens.isNotEmpty()
            && (stackOfNonNumberTokens.peek() !is OpenBracket)
        ) {
            calculateFromTopOperator()
            stackOfNonNumberTokens.pop()
        }
    }

    private fun priorityAvalanche(stopPriority: Int) {
        while (stackOfNonNumberTokens.isNotEmpty()
            && ((stackOfNonNumberTokens.peek() is BinaryInfixOperatorToken
                    && (stackOfNonNumberTokens.peek() as BinaryInfixOperatorToken).priority >= stopPriority)
                    || (stackOfNonNumberTokens.peek() is UnaryOperatorToken))
        ) {
            calculateFromTopOperator()
        }
    }

    private fun calculateFromTopOperator() {
        val topOperatorToken = stackOfNonNumberTokens.pop()
        val index = stackOfIndexes.pop()
        when {
            isUnary(index) -> {
                if (stackOfNumberTokens.size < 1) {
                    throw ArithmeticException("Not enough arguments for operator")
                }
                stackOfNumberTokens.push(
                    (topOperatorToken as UnaryOperatorToken).evaluate(
                        stackOfNumberTokens.pop()
                    )
                )
            }

            isInfix(index) -> {
                if (stackOfNumberTokens.size < 2) {
                    throw ArithmeticException("Not enough arguments for operator")
                }
                val secondNumber = stackOfNumberTokens.pop()
                val firstNumber = stackOfNumberTokens.pop()
                stackOfNumberTokens.push(
                    (topOperatorToken as BinaryInfixOperatorToken).evaluate(
                        firstNumber,
                        secondNumber
                    )
                )
            }
        }
    }

}