package com.jefisu.manualplus.core.di

import com.jefisu.manualplus.core.data.FileToUploadDao
import com.jefisu.manualplus.features_manual.data.RealmSyncRepository
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.realm.kotlin.mongodb.App

@Module
@InstallIn(ViewModelComponent::class)
object ManualModule {

    @Provides
    @ViewModelScoped
    fun provideSyncRepository(
        fileToUploadDao: FileToUploadDao,
        app: App
    ): SyncRepository {
        return RealmSyncRepository(fileToUploadDao, app)
    }
}