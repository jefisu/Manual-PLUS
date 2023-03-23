package com.jefisu.manualplus.features_manual.presentation.profile_user

import android.net.Uri
import com.jefisu.manualplus.features_manual.presentation.profile_user.util.SettingsUser

data class ProfileUserState(
    val isLoading: Boolean = false,
    val name: String = "",
    val supportMessage: String = "",
    val hospitalName: String = "",
    val hospitalAddress: String = "",
    val imagesToUpload: List<Uri> = emptyList(),
    val settings: SettingsUser = SettingsUser.EditProfile,
)