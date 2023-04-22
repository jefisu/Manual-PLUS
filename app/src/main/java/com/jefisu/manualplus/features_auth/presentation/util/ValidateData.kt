package com.jefisu.manualplus.features_auth.presentation.util

import android.util.Patterns
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.util.UiText

object ValidateData {

    data class ValidationResult(val error: UiText? = null)

    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                error = UiText.StringResource(R.string.the_email_cant_be_blank)
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                error = UiText.StringResource(R.string.thats_not_valid_email)
            )
        }
        return ValidationResult()
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                error = UiText.StringResource(R.string.the_password_needs_8_characters)
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
            password.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return ValidationResult(
                error = UiText.StringResource(R.string.the_password_not_contain_letter_and_digit)
            )
        }
        return ValidationResult()
    }

    fun validateRepeatedPassword(password: String, repeatedPassword: String): ValidationResult {
        if (password != repeatedPassword) {
            return ValidationResult(
                error = UiText.StringResource(R.string.password_not_equals)
            )
        }
        return ValidationResult()
    }
}