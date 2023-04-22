package com.jefisu.manualplus.features_manual.domain

import com.jefisu.manualplus.core.data.entity.FileToUploadEntity
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.SimpleResource
import com.jefisu.manualplus.features_manual.domain.model.Equipment
import com.jefisu.manualplus.features_manual.domain.model.SupportRequest
import com.jefisu.manualplus.features_manual.domain.model.User
import kotlinx.coroutines.flow.Flow

interface SyncRepository {
    fun getUser(): Flow<Resource<User>>
    fun getEquipments(): Flow<Resource<List<Equipment>>>
    fun getEquipmentById(id: String): Flow<Resource<Equipment>>
    suspend fun updateUserInfo(name: String): SimpleResource
    suspend fun addSupportRequest(supportRequest: SupportRequest): SimpleResource
    suspend fun addFileToUpload(file: FileToUploadEntity)
    suspend fun updateAvatarUser(remotePath: String): SimpleResource
}