package com.jefisu.manualplus.core.di

import android.app.Application
import androidx.room.Room
import com.jefisu.manualplus.BuildConfig
import com.jefisu.manualplus.core.connectivity.ConnectivityObserver
import com.jefisu.manualplus.core.connectivity.NetworkConnectivityObserver
import com.jefisu.manualplus.core.data.FileDatabase
import com.jefisu.manualplus.core.data.FileToUploadDao
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
    fun provideFileDatabase(app: Application): FileToUploadDao {
        return Room.databaseBuilder(
            app,
            FileDatabase::class.java,
            "filesToUpload_db"
        ).build().fileToUploadDao
    }
}