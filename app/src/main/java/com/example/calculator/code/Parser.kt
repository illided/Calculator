package com.example.calculator.code

object Parser {
    private val TOKEN_PARSING_REGEX = "\\*|\\+|-|/|\\(|\\)|(\\d+\\.\\d+)|(\\d+)".toRegex()

    fun parseToTokens(expression: String): List<Token> {
        val listOfTokensAsStrings =
            TOKEN_PARSING_REGEX.findAll(expression).map { it.value }.toList()

        require(
            expression.replace(
                " ",
                ""
            ).length == listOfTokensAsStrings.sumBy { it.length }
        ) { "Illegal characters was in the string" }

        val mutableListOfTokens = mutableListOf<Token>(OpenBracket)

        for (token in listOfTokensAsStrings) {
            if (token.toDoubleOrNull() == null) {
                mutableListOfTokens.add(NON_NUMBER_TOKENS.find { it.stringPresentation == token }
                    ?: throw IllegalArgumentException("Incorrect token: $token"))
            } else {
                mutableListOfTokens.add(createNumberToken(token))
            }
        }

        mutableListOfTokens.add(ClosedBracket)

        return mutableListOfTokens.toList()
    }
}
