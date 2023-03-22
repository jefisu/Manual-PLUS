package com.jefisu.manualplus.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jefisu.manualplus.core.data.database.entity.FileToUploadEntity

@Database(entities = [FileToUploadEntity::class], version = 1)
abstract class FileDatabase : RoomDatabase() {
    abstract val fileToUploadDao: FileToUploadDao
}