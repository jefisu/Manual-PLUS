package com.jefisu.manualplus.features_manual.data.mapper

import com.jefisu.manualplus.features_manual.data.dto.ConfigurationDto
import com.jefisu.manualplus.features_manual.domain.Configuration

fun ConfigurationDto.toConfiguration(): Configuration {
    return Configuration(
        title = title,
        imagePath = imagePath,
        stepByStep = stepByStep
    )
}