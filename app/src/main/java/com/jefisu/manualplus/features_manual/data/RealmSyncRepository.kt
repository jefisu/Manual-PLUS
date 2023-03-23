package com.jefisu.manualplus.features_manual.data

import com.jefisu.manualplus.core.data.FileToUploadDao
import com.jefisu.manualplus.core.data.entity.FileToUploadEntity
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.SimpleResource
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.data.dto.EquipmentDto
import com.jefisu.manualplus.features_manual.data.dto.SupportRequestDto
import com.jefisu.manualplus.features_manual.data.dto.UserDto
import com.jefisu.manualplus.features_manual.data.mapper.toEquipment
import com.jefisu.manualplus.features_manual.data.mapper.toSupportRequestDto
import com.jefisu.manualplus.features_manual.domain.Equipment
import com.jefisu.manualplus.features_manual.domain.SupportRequest
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import com.jefisu.manualplus.features_manual.domain.User
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
import org.mongodb.kbson.ObjectId

class RealmSyncRepository(
    private val fileToUploadDao: FileToUploadDao,
    app: App
) : SyncRepository {

    private var user = app.currentUser!!
    private val realm by lazy {
        val config = SyncConfiguration.Builder(
            user,
            setOf(UserDto::class, EquipmentDto::class, SupportRequestDto::class)
        ).initialSubscriptions { sub ->
            add(query = sub.query<UserDto>("_id == $0", ObjectId(user.id)))
            add(query = sub.query<EquipmentDto>())
            add(query = sub.query<SupportRequestDto>())
        }
            .log(LogLevel.ALL)
            .build()

        Realm.open(config)
    }

    override fun getUser(): Flow<Resource<User>> {
        return try {
            realm
                .query<UserDto>("_id == $0", ObjectId(user.id))
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

    override suspend fun updateUserInfo(name: String): SimpleResource {
        return try {
            realm.write {
                val documentUser = query<UserDto>("_id == $0", BsonObjectId(user.id))
                    .first()
                    .find()
                    ?: return@write Resource.Error<Unit>(UiText.DynamicString("User not found"))

                documentUser.apply {
                    this.name = name
                }
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            Resource.Error(UiText.unknownError())
        }
    }

    override suspend fun addSupportRequest(supportRequest: SupportRequest): SimpleResource {
        return try {
            realm.write {
                copyToRealm(
                    supportRequest.toSupportRequestDto().apply {
                        createdByUserId = user.id
                    }
                )
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(UiText.unknownError())
        }
    }

    override suspend fun addFileToUpload(file: FileToUploadEntity) {
        fileToUploadDao.insertFileToUpload(file)
    }
}