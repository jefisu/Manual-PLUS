package com.jefisu.manualplus.features_auth.presentation

import com.jefisu.manualplus.core.util.Resource

sealed class AuthEvent {
    object ErrorDisplayed : AuthEvent()
    data class EnterEmail(val value: String, val isSigning: Boolean) : AuthEvent()
    data class EnterPassword(val value: String, val isSigning: Boolean) : AuthEvent()
    data class EnterRepeatPassword(val value: String) : AuthEvent()
    object LoginEmail : AuthEvent()
    data class LoginGoogle(val result: Resource<String>) : AuthEvent()
    object SignUp : AuthEvent()
}
