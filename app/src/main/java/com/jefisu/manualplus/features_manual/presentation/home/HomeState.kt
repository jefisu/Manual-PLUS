package com.jefisu.manualplus.features_manual.presentation.home

import android.net.Uri
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.domain.Equipment

data class HomeState(
    val equipments: List<Pair<Equipment, Uri>> = emptyList(),
    val categories: List<String> = emptyList(),
    val error: UiText? = null
)