package com.jefisu.manualplus.features_manual.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val avatarRemotePath: String,
    val email: String,
    val createdAt: Long
): Parcelable