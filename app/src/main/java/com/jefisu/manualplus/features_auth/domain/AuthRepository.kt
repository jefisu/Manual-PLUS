package com.jefisu.manualplus.features_auth.domain

import com.jefisu.manualplus.core.util.SimpleResource

interface AuthRepository {
    suspend fun login(email: String, password: String): SimpleResource
    suspend fun loginGoogle(token: String): SimpleResource
    suspend fun signUp(email: String, password: String): SimpleResource
}