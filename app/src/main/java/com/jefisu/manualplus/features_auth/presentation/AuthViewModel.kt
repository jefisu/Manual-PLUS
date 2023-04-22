package com.jefisu.manualplus.features_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.core.connectivity.ConnectivityObserver
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.features_auth.domain.AuthRepository
import com.jefisu.manualplus.features_auth.presentation.util.ValidateData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _connectivityStatus =
        MutableStateFlow<ConnectivityObserver.Status>(ConnectivityObserver.Status.Unavailable)

    init {
        connectivityObserver.observe().onEach { status ->
            _connectivityStatus.update { status }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EnterEmail -> {
                if (event.isSigning) {
                    _state.update { it.copy(emailSignUp = event.value) }
                } else {
                    _state.update { it.copy(emailSignIn = event.value) }
                }
            }
            is AuthEvent.EnterPassword -> {
                if (event.isSigning) {
                    _state.update { it.copy(passwordSignUp = event.value) }
                } else {
                    _state.update { it.copy(passwordSignIn = event.value) }
                }
            }
            is AuthEvent.EnterRepeatPassword -> {
                _state.update { it.copy(repeatPasswordSignUp = event.value) }
            }
            is AuthEvent.ErrorDisplayed -> {
                _state.update { it.copy(error = null) }
            }
            is AuthEvent.LoginEmail -> login()
            is AuthEvent.LoginGoogle -> loginWithGoogle(event.result)
            is AuthEvent.SignUp -> signUp()
        }
    }

    private fun login() {
        if (_connectivityStatus.value is ConnectivityObserver.Status.Unavailable) {
            _state.update { it.copy(error = _connectivityStatus.value.uiText) }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    error = null,
                    isLoading = true,
                    showLoadingButton = LoadingButton.NormalLogin
                )
            }

            val result = repository.login(_state.value.emailSignIn, _state.value.passwordSignIn)
            if (result is Resource.Error) {
                _state.update {
                    it.copy(error = result.uiText)
                }
            } else {
                _state.update {
                    it.copy(canNavigate = true)
                }
            }
            _state.update {
                it.copy(isLoading = false, showLoadingButton = null)
            }
        }
    }

    private fun loginWithGoogle(tokenResult: Resource<String>) {
        if (_connectivityStatus.value is ConnectivityObserver.Status.Unavailable) {
            _state.update { it.copy(error = _connectivityStatus.value.uiText) }
            return
        }
        when (tokenResult) {
            is Resource.Error -> {
                _state.update {
                    it.copy(error = tokenResult.uiText)
                }
            }
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        error = null,
                        isLoading = true,
                        showLoadingButton = LoadingButton.Google
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    val result = repository.loginGoogle(tokenResult.data!!)
                    if (result is Resource.Error) {
                        _state.update { it.copy(error = result.uiText) }
                        return@launch
                    }
                    _state.update {
                        it.copy(
                            canNavigate = true,
                            isLoading = false,
                            showLoadingButton = null
                        )
                    }
                }
            }
        }
    }

    private fun signUp() {
        if (_connectivityStatus.value is ConnectivityObserver.Status.Unavailable) {
            _state.update { it.copy(error = _connectivityStatus.value.uiText) }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(error = null) }

            val emailResult = ValidateData.validateEmail(_state.value.emailSignUp)
            val passwordResult = ValidateData.validatePassword(_state.value.passwordSignUp)
            val repeatPasswordResult = ValidateData.validateRepeatedPassword(
                password = _state.value.passwordSignUp,
                repeatedPassword = _state.value.repeatPasswordSignUp
            )
            val validationsResult = listOf(emailResult, passwordResult, repeatPasswordResult)
            if (validationsResult.any { it.error != null }) {
                _state.update { state ->
                    state.copy(error = validationsResult.firstNotNullOf { it.error })
                }
                return@launch
            }

            _state.update { state ->
                state.copy(isLoading = true, showLoadingButton = LoadingButton.SignUp)
            }
            val result = repository.signUp(_state.value.emailSignUp, _state.value.passwordSignUp)
            if (result is Resource.Error) {
                _state.update { state ->
                    state.copy(error = result.uiText)
                }
                return@launch
            }
            _state.update {
                it.copy(
                    canNavigate = true,
                    isLoading = false,
                    showLoadingButton = null
                )
            }
        }
    }

    sealed class LoadingButton {
        object SignUp : LoadingButton()
        object Google : LoadingButton()
        object NormalLogin : LoadingButton()
    }
}