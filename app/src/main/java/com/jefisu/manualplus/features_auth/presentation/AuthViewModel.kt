package com.jefisu.manualplus.features_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.core.connectivity.ConnectivityObserver
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_auth.domain.AuthRepository
import com.jefisu.manualplus.features_auth.presentation.util.ValidateData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<UiText?>(null)
    val error = _error.asStateFlow()

    private val _navigateEvent = Channel<Boolean>()
    val navigateEvent = _navigateEvent.receiveAsFlow()

    private val _connectivityStatus =
        MutableStateFlow<ConnectivityObserver.Status>(ConnectivityObserver.Status.Unavailable)

    init {
        connectivityObserver.observe().onEach { status ->
            _connectivityStatus.update { status }
        }.launchIn(viewModelScope)
    }

    fun enteredName(value: String) {
        _signUpState.update { it.copy(repeatPassword = value) }
    }

    fun enteredEmail(value: String, isSigning: Boolean) {
        if (isSigning) {
            _signUpState.update { it.copy(email = value) }
        } else {
            _loginState.update { it.copy(email = value) }
        }
    }

    fun enteredPassword(value: String, isSigning: Boolean) {
        if (isSigning) {
            _signUpState.update {
                it.copy(password = value)
            }
        } else {
            _loginState.update { it.copy(password = value) }
        }
    }

    fun errorDisplayed() {
        _error.update { null }
    }

    fun login() {
        if (_connectivityStatus.value is ConnectivityObserver.Status.Unavailable) {
            _error.update { _connectivityStatus.value.uiText }
            return
        }
        viewModelScope.launch {
            _error.update { null }

            val emailResult = ValidateData.validateEmail(_loginState.value.email)
            val passwordResult = ValidateData.validatePassword(_loginState.value.password)
            val validationsResult = listOf(emailResult, passwordResult)
            if (validationsResult.any { it.error != null }) {
                _error.update { validationsResult.firstNotNullOf { it.error } }
                _isLoading.update { false }
                return@launch
            }

            _isLoading.update { true }
            val result = repository.login(_loginState.value.email, _loginState.value.password)
            if (result is Resource.Error) {
                _error.update { result.uiText }
            } else {
                _navigateEvent.send(true)
            }
            _isLoading.update { false }
        }
    }

    fun loginWithGoogle(tokenResult: Resource<String>) {
        if (_connectivityStatus.value is ConnectivityObserver.Status.Unavailable) {
            _error.update { _connectivityStatus.value.uiText }
            return
        }
        when (tokenResult) {
            is Resource.Error -> _error.update { tokenResult.uiText }
            is Resource.Success -> {
                _isLoading.update { true }
                viewModelScope.launch {
                    val result = repository.loginGoogle(tokenResult.data!!)
                    if (result is Resource.Error) {
                        _error.update { result.uiText }
                        return@launch
                    }
                    _navigateEvent.send(true)
                    _isLoading.update { false }
                }
            }
        }
    }

    fun signUp() {
        if (_connectivityStatus.value is ConnectivityObserver.Status.Unavailable) {
            _error.update { _connectivityStatus.value.uiText }
            return
        }
        viewModelScope.launch {
            _error.update { null }

            val emailResult = ValidateData.validateEmail(_signUpState.value.email)
            val passwordResult = ValidateData.validatePassword(_signUpState.value.password)
            val repeatPasswordResult = ValidateData.validateRepeatedPassword(
                password = _signUpState.value.password,
                repeatedPassword = _signUpState.value.repeatPassword
            )
            val validationsResult = listOf(emailResult, passwordResult, repeatPasswordResult)
            if (validationsResult.any { it.error != null }) {
                _error.update { validationsResult.firstNotNullOf { it.error } }
                _isLoading.update { false }
                return@launch
            }

            _isLoading.update { true }
            val result = repository.signUp(_signUpState.value.email, _signUpState.value.password)
            if (result is Resource.Error) {
                _error.update { result.uiText }
            } else {
                _navigateEvent.send(true)
            }
            _isLoading.update { false }
        }
    }
}