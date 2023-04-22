package com.jefisu.manualplus.features_manual.presentation.detail

import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.domain.model.Equipment

data class DetailState(
    val equipment: Equipment? = null,
    val imageUrl: String = "",
    val error: UiText? = null,
    val isLoading: Boolean = false
)