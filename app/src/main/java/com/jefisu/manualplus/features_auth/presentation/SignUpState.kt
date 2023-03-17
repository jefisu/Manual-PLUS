package com.jefisu.manualplus.features_auth.presentation

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)