package com.jefisu.manualplus.features_manual.data.dto

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class ConfigurationDto : RealmObject {
    @PrimaryKey
    var _id = ObjectId()
    var equipmentId = ""
    var title: String = ""
    var imagePath: String = ""
    var stepByStep: RealmList<String> = realmListOf()
    var orderNumber = 0
}