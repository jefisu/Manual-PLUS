package com.jefisu.manualplus.features_manual.presentation.detail

import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.domain.Configuration
import com.jefisu.manualplus.features_manual.domain.Equipment

data class DetailState(
    val equipment: Equipment? = null,
    val configurations: List<Configuration> = emptyList(),
    val error: UiText? = null
)