package com.fifty.formvalidationmvvmclean.domain.usecase

data class ValidationResult(
    val successful: Boolean,
    val errorMessage:String? = null
)
