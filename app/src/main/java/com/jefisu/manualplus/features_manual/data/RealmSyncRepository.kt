package com.jefisu.manualplus.features_manual.data

import com.jefisu.manualplus.core.data.User
import com.jefisu.manualplus.core.data.UserDto
import com.jefisu.manualplus.core.data.toUser
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.domain.Equipment
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.BsonObjectId

class RealmSyncRepository(
    app: App
) : SyncRepository {

    private val user = app.currentUser!!
    private lateinit var realm: Realm

    override fun configureRealm() {
        val config = SyncConfiguration.Builder(
            user,
            setOf(UserDto::class, EquipmentDto::class)
        ).initialSubscriptions { sub ->
            add(query = sub.query<UserDto>("_id == $0", BsonObjectId(user.id)))
            add(query = sub.query<EquipmentDto>())
        }.log(LogLevel.ALL).build()
        realm = Realm.open(config)
    }

    override fun getUser(): Flow<Resource<User>> {
        return try {
            realm
                .query<UserDto>("_id == $0", BsonObjectId(user.id))
                .first()
                .asFlow()
                .map { Resource.Success(it.obj?.toUser()) }
                .flowOn(Dispatchers.IO)
        } catch (e: Exception) {
            flowOf(Resource.Error(UiText.unknownError()))
        }
    }

    override fun getEquipments(): Flow<Resource<List<Equipment>>> {
        return try {
            realm
                .query<EquipmentDto>()
                .asFlow()
                .map { result -> Resource.Success(result.list.map { it.toEquipment() }) }
                .flowOn(Dispatchers.IO)
        } catch (e: Exception) {
            flowOf(Resource.Error(UiText.unknownError()))
        }
    }
}