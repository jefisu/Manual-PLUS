package com.jefisu.manualplus.features_manual.presentation.avatar_user

import com.jefisu.manualplus.core.util.UiText

data class AvatarsUserState(
    val avatar: String = "",
    val availableAvatars: List<String> = emptyList(),
    val error: UiText? = null,
    val isLoading: Boolean = false,
    val uiEvent: AvatarsUserViewModel.UiEvent? = null
)