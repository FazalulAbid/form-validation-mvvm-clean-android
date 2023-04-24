package com.fifty.formvalidationmvvmclean.domain.usecase

import android.util.Patterns

class ValidateTermsUseCase {
    fun execute(acceptedTerms: Boolean): ValidationResult {
        if (!acceptedTerms) {
            return ValidationResult(
                successful = false,
                errorMessage = "Please accept the terms"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}