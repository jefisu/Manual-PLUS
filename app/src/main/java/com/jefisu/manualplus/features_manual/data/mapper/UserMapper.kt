package com.jefisu.manualplus.features_manual.data.mapper

import com.jefisu.manualplus.features_manual.data.dto.UserDto
import com.jefisu.manualplus.features_manual.domain.model.User

fun UserDto.toUser(): User {
    return User(
        id = _id.toString(),
        name = name,
        avatarRemotePath = avatar,
        email = email,
        createdAt = createdAt
    )
}