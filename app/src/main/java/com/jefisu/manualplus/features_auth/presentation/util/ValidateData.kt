package com.jefisu.manualplus.features_auth.presentation.util

import android.util.Patterns
import com.jefisu.manualplus.core.util.UiText

object ValidateData {

    data class ValidationResult(val error: UiText? = null)

    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                error = UiText.DynamicString("The email can't be blank")
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                error = UiText.DynamicString("That's not a valid email")
            )
        }
        return ValidationResult()
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                error = UiText.DynamicString("The password needs to consist of at least 8 characters")
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return ValidationResult(
                error = UiText.DynamicString("The password needs to contain at least one letter and digit")
            )
        }
        return ValidationResult()
    }

    fun validateRepeatedPassword(password: String, repeatedPassword: String): ValidationResult {
        if (password != repeatedPassword) {
            return ValidationResult(
                error = UiText.DynamicString("The passwords don't match")
            )
        }
        return ValidationResult()
    }
}