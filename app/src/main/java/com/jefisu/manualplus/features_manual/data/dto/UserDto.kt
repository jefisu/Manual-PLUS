package com.jefisu.manualplus.features_manual.data.dto

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class UserDto : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var avatar: String = ""
    var email: String = ""
    var createdAt: Long = 0L
}