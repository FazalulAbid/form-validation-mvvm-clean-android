package com.fifty.formvalidationmvvmclean.domain.usecase

import android.util.Patterns

class ValidateRepeatedPasswordUseCase {
    fun execute(password: String, repeatedPassword:String): ValidationResult {
        if (password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}