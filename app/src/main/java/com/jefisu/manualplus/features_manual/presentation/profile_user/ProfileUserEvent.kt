package com.jefisu.manualplus.features_manual.presentation.profile_user

import android.net.Uri
import com.jefisu.manualplus.features_manual.presentation.profile_user.util.SettingsUser

sealed class ProfileUserEvent {
    data class EnterName(val value: String) : ProfileUserEvent()
    data class EnterHospitalName(val value: String) : ProfileUserEvent()
    data class EnterHospitalAddress(val value: String) : ProfileUserEvent()
    data class EnterSupportMessage(val value: String) : ProfileUserEvent()
    data class SelectSetting(val value: SettingsUser) : ProfileUserEvent()
    data class EnterImages(val value: List<Uri>) : ProfileUserEvent()
    object Logout : ProfileUserEvent()
    object SendSupportRequest : ProfileUserEvent()
    object SaveChanges : ProfileUserEvent()
}
