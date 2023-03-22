package com.jefisu.manualplus.features_user.presentation.profile_user

import android.net.Uri
import com.jefisu.manualplus.core.util.Theme
import com.jefisu.manualplus.features_user.presentation.profile_user.util.SettingsUser

data class ProfileUserState(
    val isLoading: Boolean = false,
    val name: String = "",
    val supportMessage: String = "",
    val hospitalName: String = "",
    val hospitalAddress: String = "",
    val imagesToUpload: List<Pair<Uri, String>> = emptyList(),
    val theme: Theme = Theme.SystemDefault,
    val settings: SettingsUser = SettingsUser.EditProfile,
)