package com.jefisu.manualplus.features_manual.presentation.avatar_user

sealed class AvatarsUserEvent {
    data class EnterAvatar(val value: String) : AvatarsUserEvent()
    object SaveUserAvatar : AvatarsUserEvent()
    object ErrorDisplayed : AvatarsUserEvent()
}
