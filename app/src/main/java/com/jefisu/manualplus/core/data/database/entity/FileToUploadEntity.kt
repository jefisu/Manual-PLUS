package com.jefisu.manualplus.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class FileToUploadEntity(
    val remotePath: String,
    val fileBytes: ByteArray,
    val sessionUri: String,
    @PrimaryKey val id: String = UUID.randomUUID().toString()
)