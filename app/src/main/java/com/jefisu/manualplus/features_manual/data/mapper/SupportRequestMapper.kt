package com.jefisu.manualplus.features_manual.data.mapper

import com.jefisu.manualplus.features_manual.data.dto.SupportRequestDto
import com.jefisu.manualplus.features_manual.domain.model.SupportRequest
import io.realm.kotlin.ext.toRealmList

fun SupportRequest.toSupportRequestDto(): SupportRequestDto {
    return SupportRequestDto().apply {
        _id = id
        hospitalName = this@toSupportRequestDto.hospitalName
        hospitalAddress = this@toSupportRequestDto.hospitalAddress
        problem = this@toSupportRequestDto.problem
        remotePathImages = this@toSupportRequestDto.remotePathImages.toRealmList()
    }
}