package com.jefisu.manualplus.features_manual.presentation.home

import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.domain.Equipment
import com.jefisu.manualplus.features_manual.domain.User

data class HomeState(
    val equipments: List<Equipment> = emptyList(),
    val categories: List<String> = emptyList(),
    val user: User? = null,
    val error: UiText? = null
)