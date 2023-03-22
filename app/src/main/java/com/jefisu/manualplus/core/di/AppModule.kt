package com.jefisu.manualplus.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.jefisu.manualplus.BuildConfig
import com.jefisu.manualplus.core.connectivity.ConnectivityObserver
import com.jefisu.manualplus.core.connectivity.NetworkConnectivityObserver
import com.jefisu.manualplus.core.data.SharedRepositoryImpl
import com.jefisu.manualplus.core.data.database.FileDatabase
import com.jefisu.manualplus.core.data.database.FileToUploadDao
import com.jefisu.manualplus.core.domain.SharedRepository
import com.jefisu.manualplus.features_manual.data.RealmManualRepository
import com.jefisu.manualplus.features_manual.domain.ManualRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.mongodb.App
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRealmApp() = App.create(BuildConfig.app_id)

    @Provides
    @Singleton
    fun provideConnectivityObserver(app: Application): ConnectivityObserver {
        return NetworkConnectivityObserver(app)
    }

    @Provides
    @Singleton
    fun provideSyncRepository(): ManualRepository {
        return RealmManualRepository()
    }

    @Provides
    @Singleton
    fun provideSharedRepository(app: App, dao: FileToUploadDao): SharedRepository {
        return SharedRepositoryImpl(app, dao)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFileDatabase(app: Application): FileToUploadDao {
        return Room.databaseBuilder(
            app,
            FileDatabase::class.java,
            "filesToUpload_db"
        ).build().fileToUploadDao
    }
}