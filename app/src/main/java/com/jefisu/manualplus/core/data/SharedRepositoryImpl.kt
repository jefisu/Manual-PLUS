package com.jefisu.manualplus.core.data

import com.jefisu.manualplus.core.data.database.FileToUploadDao
import com.jefisu.manualplus.core.data.database.entity.FileToUploadEntity
import com.jefisu.manualplus.core.domain.SharedRepository
import com.jefisu.manualplus.core.domain.User
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.UiText
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.BsonObjectId

class SharedRepositoryImpl(
    private val app: App,
    private val dao: FileToUploadDao
) : SharedRepository {

    private val userId = app.currentUser!!.id
    private val realm = MongoClient.realm

    override fun getUser(): Flow<Resource<User>> {
        return try {
            realm
                .query<UserDto>("_id == $0", BsonObjectId(userId))
                .first()
                .asFlow()
                .map { Resource.Success(it.obj?.toUser()) }
                .flowOn(Dispatchers.IO)
        } catch (e: Exception) {
            flowOf(Resource.Error(UiText.unknownError()))
        }
    }

    override suspend fun addFileToUpload(file: FileToUploadEntity) {
        dao.insertFileToUpload(file)
    }

    override suspend fun getAllFileToUpload(): List<FileToUploadEntity> {
        return dao.getAllFileToUpload()
    }

    override suspend fun deleteFileToUpload(file: FileToUploadEntity) {
        deleteFileToUpload(file)
    }
}