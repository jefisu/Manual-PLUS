package com.jefisu.manualplus.core.data

import com.jefisu.manualplus.core.domain.User
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

fun UserDto.toUser(): User {
    return User(
        id = _id.toString(),
        name = name,
        avatarRemotePath = avatar,
        email = email,
        createdAt = createdAt
    )
}