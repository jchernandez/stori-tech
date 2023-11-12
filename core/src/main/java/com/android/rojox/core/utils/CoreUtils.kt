package com.android.rojox.core.utils

import android.util.Patterns
import kotlin.random.Random

object CoreUtils {

    private const val PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,}\$"


    fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return PASSWORD_REGEX.toRegex().matches(password)
    }

    fun generateAccountNumber(): String {
        val accountNumber = StringBuilder()

        repeat(16) {
            accountNumber.append(Random.nextInt(10))
        }

        while (accountNumber.length < 16) {
            accountNumber.insert(0, '0')
        }

        return accountNumber.toString()
    }

}