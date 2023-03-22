package com.jefisu.manualplus.core.data

import com.jefisu.manualplus.core.domain.User

fun UserDto.toUser(): User {
    return User(
        id = _id.toString(),
        name = name,
        avatarRemotePath = avatar,
        email = email,
        createdAt = createdAt
    )
}