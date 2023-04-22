package com.jefisu.manualplus.features_manual.data.dto

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId

open class EquipmentDto : RealmObject {
    @PrimaryKey
    var _id = BsonObjectId()
    var name = ""
    var description = ""
    var image = ""
    var serialNumber = 0
    var releaseYear = 0
    var category = ""
    var createdAt = System.currentTimeMillis()
    var instructionsConfig = realmListOf<ConfigurationDto>()
}