package com.jefisu.manualplus.features_user.presentation.domain

import com.jefisu.manualplus.core.util.SimpleResource

interface ProfileRepository {
    suspend fun updateUserInfo(name: String): SimpleResource
}