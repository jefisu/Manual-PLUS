package com.jefisu.manualplus.core.data

import androidx.room.*
import com.jefisu.manualplus.core.data.entity.FileToUploadEntity

@Dao
interface FileToUploadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFileToUpload(fileToUpload: FileToUploadEntity)

    @Query("SELECT * FROM FileToUploadEntity")
    suspend fun getAllFileToUpload(): List<FileToUploadEntity>

    @Delete
    suspend fun deleteFileToUpload(file: FileToUploadEntity)
}