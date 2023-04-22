package com.jefisu.manualplus.features_manual.data.dto

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject

class ConfigurationDto : EmbeddedRealmObject {
    var title = ""
    var configItems = realmListOf<ConfigItemDto>()
    var orderNumber = 0
}