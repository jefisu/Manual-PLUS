package com.jefisu.manualplus.features_user.presentation.profile_user

import android.net.Uri
import com.jefisu.manualplus.core.util.Theme

data class ProfileUserState(
    val name: String = "",
    val supportMessage: String = "",
    val imagesToUpload: List<Uri> = emptyList(),
    val theme: Theme = Theme.SystemDefault
)