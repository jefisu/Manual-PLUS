package com.jefisu.manualplus.features_manual.domain

import com.jefisu.manualplus.core.data.entity.FileToUploadEntity
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface SyncRepository {
    fun getUser(): Flow<Resource<User>>
    fun getEquipments(): Flow<Resource<List<Equipment>>>
    suspend fun getConfigurationEquipment(id: String): Resource<List<Configuration>>
    suspend fun updateUserInfo(name: String): SimpleResource
    suspend fun addSupportRequest(supportRequest: SupportRequest): SimpleResource
    suspend fun addFileToUpload(file: FileToUploadEntity)
    suspend fun updateAvatarUser(remotePath: String): SimpleResource
}