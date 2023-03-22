package com.jefisu.manualplus.features_user.presentation.data

import com.jefisu.manualplus.core.data.MongoClient
import com.jefisu.manualplus.core.data.UserDto
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.SimpleResource
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_user.presentation.domain.ProfileRepository
import com.jefisu.manualplus.features_user.presentation.domain.SupportRequest
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import org.mongodb.kbson.BsonObjectId

class ProfileRepositoryImpl(
    app: App
) : ProfileRepository {

    private val user = app.currentUser!!
    private val realm = MongoClient.realm

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
}