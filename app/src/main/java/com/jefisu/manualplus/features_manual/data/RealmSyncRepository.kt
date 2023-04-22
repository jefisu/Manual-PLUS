package com.jefisu.manualplus.features_manual.data

import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.data.FileToUploadDao
import com.jefisu.manualplus.core.data.entity.FileToUploadEntity
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.SimpleResource
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.data.dto.ConfigItemDto
import com.jefisu.manualplus.features_manual.data.dto.ConfigurationDto
import com.jefisu.manualplus.features_manual.data.dto.EquipmentDto
import com.jefisu.manualplus.features_manual.data.dto.SupportRequestDto
import com.jefisu.manualplus.features_manual.data.dto.UserDto
import com.jefisu.manualplus.features_manual.data.mapper.toEquipment
import com.jefisu.manualplus.features_manual.data.mapper.toSupportRequestDto
import com.jefisu.manualplus.features_manual.data.mapper.toUser
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import com.jefisu.manualplus.features_manual.domain.model.Equipment
import com.jefisu.manualplus.features_manual.domain.model.SupportRequest
import com.jefisu.manualplus.features_manual.domain.model.User
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.BsonObjectId

class RealmSyncRepository(
    private val fileToUploadDao: FileToUploadDao,
    app: App
) : SyncRepository {

    private var user = app.currentUser!!
    private val realm by lazy {
        val config = SyncConfiguration.Builder(
            user,
            setOf(
                UserDto::class,
                EquipmentDto::class,
                SupportRequestDto::class,
                ConfigurationDto::class,
                ConfigItemDto::class
            )
        ).initialSubscriptions { sub ->
            add(query = sub.query<UserDto>("_id == $0", BsonObjectId(user.id)))
            add(query = sub.query<EquipmentDto>())
            add(query = sub.query<SupportRequestDto>())
        }
            .schemaVersion(2)
            .log(LogLevel.ALL)
            .build()

        Realm.open(config)
    }

    override fun getUser(): Flow<Resource<User>> {
        return try {
            realm
                .query<UserDto>("_id == $0", BsonObjectId(user.id))
                .first()
                .asFlow()
                .map { Resource.Success(it.obj?.toUser()) }
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
        } catch (e: Exception) {
            flowOf(Resource.Error(UiText.unknownError()))
        }
    }

    override fun getEquipmentById(id: String): Flow<Resource<Equipment>> {
        return try {
            realm
                .query<EquipmentDto>("_id == $0", BsonObjectId(id))
                .asFlow()
                .map {
                    Resource.Success(it.list.first().toEquipment())
                }
        } catch (e: Exception) {
            flowOf(Resource.Error(UiText.unknownError()))
        }
    }

    override suspend fun updateUserInfo(name: String): SimpleResource {
        return try {
            realm.write {
                query<UserDto>("_id == $0", BsonObjectId(user.id))
                    .find()
                    .first()
                    .apply {
                        this.name = name
                    }
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            Resource.Error(
                UiText.StringResource(R.string.an_error_occurred_while_updating_user_information)
            )
        }
    }

    override suspend fun addSupportRequest(supportRequest: SupportRequest): SimpleResource {
        return try {
            realm.write {
                copyToRealm(supportRequest.toSupportRequestDto()).apply {
                    createdByUserId = user.id
                }
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(
                UiText.StringResource(R.string.an_error_occurred_while_submitting_the_support_request)
            )
        }
    }

    override suspend fun addFileToUpload(file: FileToUploadEntity) {
        fileToUploadDao.insertFileToUpload(file)
    }

    override suspend fun updateAvatarUser(remotePath: String): SimpleResource {
        return try {
            realm.write {
                query<UserDto>("_id == $0", BsonObjectId(user.id))
                    .find()
                    .first()
                    .apply {
                        avatar = remotePath
                    }
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            Resource.Error(
                UiText.StringResource(R.string.an_error_occurred_while_updating_the_avatar)
            )
        }
    }
}