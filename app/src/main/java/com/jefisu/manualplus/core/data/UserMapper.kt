package com.jefisu.manualplus.core.data

fun UserDto.toUser(): User {
    return User(
        id = _id.toString(),
        name = name,
        avatar = avatar,
        email = email
    )
}