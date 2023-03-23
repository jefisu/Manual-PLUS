package com.jefisu.manualplus.features_manual.presentation.profile_user.util

import androidx.annotation.DrawableRes
import com.jefisu.manualplus.R

enum class SettingsUser(val value: String, @DrawableRes val icon: Int) {
    EditProfile("Edit Profile", R.drawable.ic_user_edit),
    ContactUs("Contact us", R.drawable.ic_contact_us),
    Logout("Logout", R.drawable.ic_logout)
}