package com.jefisu.manualplus.features_manual.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Configuration(
    val title: String,
    val imagePath: String,
    val stepByStep: List<String>
) : Parcelable