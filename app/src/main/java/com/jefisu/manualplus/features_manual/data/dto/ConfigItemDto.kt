package com.jefisu.manualplus.features_manual.data.dto

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject

class ConfigItemDto : EmbeddedRealmObject {
    var title = ""
    var imagesRemotePath = realmListOf<String>()
    var attentionText = ""
    var order = 0
}