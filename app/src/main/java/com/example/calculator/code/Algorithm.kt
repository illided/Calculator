package com.example.calculator.code

import java.util.*

//Данный алгоритм поддерживает только инфиксные операторы и унарные операторы
//Если надо добавить операторы с новыми типами поведения, придется писать новый алгоритм
class Algorithm(private val expression: List<Token>) {
    private val stackOfNumberTokens: Stack<NumberToken> = Stack()
    private val stackOfNonNumberTokens: Stack<NonNumberToken> = Stack()

    inner class NonNumberToken(val index: Int, val token: Token)

    fun evaluate(): Double {
        checkForBinaryUnaryOperatorsMixUsage()
        checkForCorrectBracketUsage()


        expression.mapIndexed { index, token ->
            when {
                token is OpenBracket -> {
                    stackOfNonNumberTokens.push(NonNumberToken(index, OpenBracket))
                }
                token is ClosedBracket -> {
                    closedBracketAvalanche()
                }
                isUnary(index) -> {
                    stackOfNonNumberTokens.push(NonNumberToken(index, token))
                }
                isInfix(index) -> {
                    priorityAvalanche((token as BinaryInfixOperatorToken).priority)
                    stackOfNonNumberTokens.push(NonNumberToken(index, token))
                }
                token is NumberToken -> {
                    stackOfNumberTokens.push(token)
                }
                else -> throw ArithmeticException("Token was received but no behaviour associated")
            }
        }

        if (stackOfNonNumberTokens.size != 0 || stackOfNumberTokens.size != 1) {
            throw ArithmeticException("Some tokens left in stack")
        } else {
            return stackOfNumberTokens.pop().value
        }
    }

    private fun checkForBinaryUnaryOperatorsMixUsage() {
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

    private fun checkForCorrectBracketUsage() {
        var numOfOpenBrackets = 0
        for (token in expression) {
            if (token is OpenBracket) {
                numOfOpenBrackets++
            } else if (token is ClosedBracket) {
                if (numOfOpenBrackets > 0) {
                    numOfOpenBrackets--
                } else {
                    throw ArithmeticException("Wrong bracket usage")
                }
            }
        }
        if (numOfOpenBrackets != 0) {
            throw ArithmeticException("Wrong bracket usage")
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
            && (stackOfNonNumberTokens.peek().token !is OpenBracket)
        ) {
            calculateFromTopOperator()
        }
        stackOfNonNumberTokens.pop()
    }

    private fun priorityAvalanche(stopPriority: Int) {
        while (stackOfNonNumberTokens.isNotEmpty()
            && ((isInfix(stackOfNonNumberTokens.peek().index)
                    && (stackOfNonNumberTokens.peek().token as BinaryInfixOperatorToken).priority >= stopPriority)
                    || (isUnary(stackOfNonNumberTokens.peek().index)))
        ) {
            calculateFromTopOperator()
        }
    }

    private fun calculateFromTopOperator() {
        val topOperatorToken = stackOfNonNumberTokens.pop()
        val index = topOperatorToken.index
        when {
            isUnary(index) -> {
                if (stackOfNumberTokens.size < 1) {
                    throw ArithmeticException("Not enough arguments for operator")
                }
                stackOfNumberTokens.push(
                    (topOperatorToken.token as UnaryOperatorToken).evaluate(
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
                    (topOperatorToken.token as BinaryInfixOperatorToken).evaluate(
                        firstNumber,
                        secondNumber
                    )
                )
            }
        }
    }

}