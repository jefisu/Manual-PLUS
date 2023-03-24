package com.jefisu.manualplus.features_manual.presentation.avatar_user

import android.net.Uri
import com.jefisu.manualplus.core.util.UiText

data class AvatarsUserState(
    val avatar: Uri = Uri.EMPTY,
    val availableAvatars: List<Uri> = emptyList(),
    val error: UiText? = null,
    val isLoading: Boolean = false
)