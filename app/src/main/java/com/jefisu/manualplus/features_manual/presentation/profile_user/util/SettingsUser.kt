package com.jefisu.manualplus.features_manual.presentation.profile_user.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jefisu.manualplus.R

enum class SettingsUser(@StringRes val res: Int, @DrawableRes val icon: Int) {
    EditProfile(R.string.edit_profile, R.drawable.ic_user_edit),
    ContactUs(R.string.contact_us, R.drawable.ic_contact_us),
    Logout(R.string.logout, R.drawable.ic_logout)
}