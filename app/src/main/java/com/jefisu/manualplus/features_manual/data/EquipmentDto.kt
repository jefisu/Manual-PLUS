package com.jefisu.manualplus.features_manual.data

import com.jefisu.manualplus.features_manual.domain.Equipment
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
    var stepByStep = realmListOf<String>()
    var createdAt = System.currentTimeMillis()

    fun toEquipment() = Equipment(
        id = _id.toString(),
        name = name,
        image = image,
        description = description,
        serialNumber = serialNumber,
        releaseYear = releaseYear,
        category = category,
        stepByStep = stepByStep
    )
}