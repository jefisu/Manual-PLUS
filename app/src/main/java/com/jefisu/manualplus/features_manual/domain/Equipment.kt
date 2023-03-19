package com.jefisu.manualplus.features_manual.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Equipment(
    val id: String,
    val name: String,
    val image: String,
    val description: String,
    val serialNumber: Int,
    val releaseYear: Int,
    val category: String,
    val stepByStep: List<String>
): Parcelable
