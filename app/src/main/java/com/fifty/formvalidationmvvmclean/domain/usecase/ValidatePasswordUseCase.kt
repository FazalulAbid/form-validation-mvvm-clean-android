package com.fifty.formvalidationmvvmclean.domain.usecase

import android.util.Patterns

class ValidatePasswordUseCase {
    fun execute(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least of 8 characters"
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to contain at least a letter and digit"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}