package com.jefisu.manualplus.core.data

import com.jefisu.manualplus.BuildConfig
import com.jefisu.manualplus.features_manual.data.EquipmentDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoClient {

    private val app = App.create(BuildConfig.app_id)
    private val user = app.currentUser!!
    lateinit var realm: Realm

    fun configureRealm() {
        val config = SyncConfiguration.Builder(
            user,
            setOf(UserDto::class, EquipmentDto::class)
        ).initialSubscriptions { sub ->
            add(query = sub.query<UserDto>("_id == $0", org.mongodb.kbson.BsonObjectId(user.id)))
            add(query = sub.query<EquipmentDto>())
        }.log(LogLevel.ALL).build()
        realm = Realm.open(config)
    }
}