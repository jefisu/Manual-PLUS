package com.jefisu.manualplus.features_manual.presentation.detail

import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.domain.Configuration

data class DetailState(
    val configurations: List<Configuration> = emptyList(),
    val error: UiText? = null
)