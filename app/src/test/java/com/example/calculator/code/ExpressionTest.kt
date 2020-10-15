package com.example.calculator.code

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException


internal class ExpressionTest {
    @Test
    fun value_correctExpression_onePositiveNumber() {
        assertEquals(100.0, Expression("100").value, 0.001)
    }

    @Test
    fun value_correctExpression_negativeNumber() {
        assertEquals(-100.0, Expression("-100").value, 0.001)
    }

    @Test
    fun value_correctExpressionMultiplyFirst() {
        assertEquals(100.0, Expression("25 * 3 + 20 + 5").value, 0.001)
    }

    @Test
    fun value_correctExpressionMultiplyLater() {
        assertEquals(31.0, Expression("56-55+6*5").value, 0.001)
    }

    @Test
    fun value_correctExpressionWithBrackets() {
        assertEquals(12.0, Expression("(2+4)*2").value, 0.001)
    }

    @Test
    fun value_correctExpressionUnaryMinus() {
        assertEquals(-12.0, Expression("-15+3").value, 0.001)
    }

    @Test
    fun value_correctExpressionUnaryMinusWithBrackets() {
        assertEquals(-12.0, Expression("-(3+3) - 6").value, 0.001)
    }

    @Test
    fun value_wrongExpressionIllegalCharacters_exceptionCaught() {
        assertThrows<IllegalArgumentException> { Expression("hello").value }
    }

    @Test
    fun value_wrongExpressionWrongUseOfOperators_exceptionCaught() {
        assertThrows<ArithmeticException> { Expression("5+-3").value }
    }

    @Test
    fun value_wrongExpressionWrongUseOfBrackets_exceptionCaught() {
        assertThrows<ArithmeticException> { Expression("6+(3-2))").value }
    }
}