package com.jefisu.manualplus.features_manual.data.mapper

import com.jefisu.manualplus.features_manual.data.dto.EquipmentDto
import com.jefisu.manualplus.features_manual.domain.Equipment
import java.text.DateFormat
import java.util.Locale

fun EquipmentDto.toEquipment() = Equipment(
    id = _id.toHexString(),
    name = name,
    image = image,
    description = description,
    serialNumber = serialNumber,
    releaseYear = releaseYear,
    category = category,
    createdAt = DateFormat
        .getDateInstance(DateFormat.SHORT, Locale.getDefault())
        .format(createdAt)
)