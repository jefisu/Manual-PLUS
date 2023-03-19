package com.jefisu.manualplus.features_manual.presentation.home

import android.net.Uri
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.domain.Equipment

data class HomeState(
    val avatarUser: Uri? = null,
    val equipments: List<Equipment> = emptyList(),
    val categories: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val categorySelected: String = ""
)