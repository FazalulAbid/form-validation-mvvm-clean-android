package com.fifty.formvalidationmvvmclean.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fifty.formvalidationmvvmclean.domain.usecase.ValidateEmailUseCase
import com.fifty.formvalidationmvvmclean.domain.usecase.ValidatePasswordUseCase
import com.fifty.formvalidationmvvmclean.domain.usecase.ValidateRepeatedPasswordUseCase
import com.fifty.formvalidationmvvmclean.domain.usecase.ValidateTermsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase = ValidateEmailUseCase(),
    private val validatePasswordUseCase: ValidatePasswordUseCase = ValidatePasswordUseCase(),
    private val validateRepeatedPasswordUseCase: ValidateRepeatedPasswordUseCase = ValidateRepeatedPasswordUseCase(),
    private val validateTermsUseCase: ValidateTermsUseCase = ValidateTermsUseCase()
) : ViewModel() {

    var state by mutableStateOf(RegistrationFormState())

    private val validateEventChannel = Channel<ValidationEvent>()
    val validationEvents = validateEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                state = state.copy(repeatedPassword = event.repeatedPassword)
            }
            is RegistrationFormEvent.AcceptTerms -> {
                state = state.copy(acceptedTerms = event.isAccepted)
            }
            RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmailUseCase.execute(state.email)
        val passwordResult = validatePasswordUseCase.execute(state.password)
        val repeatedPasswordResult = validateRepeatedPasswordUseCase.execute(
            state.password,
            state.repeatedPassword
        )
        val termsResult = validateTermsUseCase.execute(state.acceptedTerms)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            termsResult
        ).any { !it.successful }

        if (hasError) {
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                termsError = termsResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validateEventChannel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}