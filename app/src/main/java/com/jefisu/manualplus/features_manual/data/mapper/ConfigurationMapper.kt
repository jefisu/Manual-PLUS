package com.jefisu.manualplus.features_manual.data.mapper

import com.jefisu.manualplus.features_manual.data.dto.ConfigurationDto
import com.jefisu.manualplus.features_manual.domain.model.Configuration

fun ConfigurationDto.toConfiguration(): Configuration {
    return Configuration(
        title = title,
        configItems = configItems
            .sortedBy { it.order }
            .map { it.toConfigItem() }
    )
}