package com.jefisu.manualplus.features_manual.data.mapper

import com.jefisu.manualplus.features_manual.data.dto.ConfigItemDto
import com.jefisu.manualplus.features_manual.domain.model.ConfigItem

fun ConfigItemDto.toConfigItem(): ConfigItem {
    return ConfigItem(
        title = title,
        imagesRemotePath = imagesRemotePath,
        attentionText = attentionText
    )
}