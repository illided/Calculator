package com.example.calculator.code

import org.junit.Test

internal class ParserTest {

    @Test
    fun parseToTokens() {
        val a = Parser.parseToTokens("- 89 + 123.78 / 89 * 13")
    }
}