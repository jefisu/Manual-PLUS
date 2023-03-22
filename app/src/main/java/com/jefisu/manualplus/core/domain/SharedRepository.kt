package com.jefisu.manualplus.core.domain

import com.jefisu.manualplus.core.data.database.entity.FileToUploadEntity
import com.jefisu.manualplus.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface SharedRepository {
    fun getUser(): Flow<Resource<User>>
    suspend fun addFileToUpload(file: FileToUploadEntity)
    suspend fun getAllFileToUpload(): List<FileToUploadEntity>
    suspend fun deleteFileToUpload(file: FileToUploadEntity)
}