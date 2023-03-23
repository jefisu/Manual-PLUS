package com.jefisu.manualplus.features_manual.data

import com.jefisu.manualplus.features_manual.data.dto.UserDto
import com.jefisu.manualplus.features_manual.domain.User

fun UserDto.toUser(): User {
    return User(
        id = _id.toString(),
        name = name,
        avatarRemotePath = avatar,
        email = email,
        createdAt = createdAt
    )
}