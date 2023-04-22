package com.jefisu.manualplus.features_manual.presentation.home

import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.domain.model.Equipment

data class HomeState(
    val equipments: List<Equipment> = emptyList(),
    val categories: List<String> = emptyList(),
    val error: UiText? = null,
    val isLoading: Boolean = false
)