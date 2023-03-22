package com.jefisu.manualplus.features_user.presentation.data

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class SupportRequestDto : RealmObject {
    @PrimaryKey
    var _id = ObjectId()
    var hospitalName = ""
    var hospitalAddress = ""
    var problem = ""
    var remotePathImages = realmListOf<String>()
    var createdByUserId = ""
    var createdAt = System.currentTimeMillis()
}