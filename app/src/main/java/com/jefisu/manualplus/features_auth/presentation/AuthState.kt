package com.jefisu.manualplus.features_auth.presentation

import com.jefisu.manualplus.core.util.UiText

data class AuthState(
    val emailSignIn: String = "",
    val passwordSignIn: String = "",
    val emailSignUp: String = "",
    val passwordSignUp: String = "",
    val repeatPasswordSignUp: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val canNavigate: Boolean = false,
    val showLoadingButton: AuthViewModel.LoadingButton? = null
)