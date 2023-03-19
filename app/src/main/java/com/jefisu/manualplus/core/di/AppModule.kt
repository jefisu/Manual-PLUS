package com.jefisu.manualplus.core.di

import android.app.Application
import com.jefisu.manualplus.BuildConfig
import com.jefisu.manualplus.core.connectivity.ConnectivityObserver
import com.jefisu.manualplus.core.connectivity.NetworkConnectivityObserver
import com.jefisu.manualplus.features_manual.data.RealmSyncRepository
import com.jefisu.manualplus.features_manual.domain.SyncRepository
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
    fun provideSyncRepository(
        app: App
    ): SyncRepository {
        return RealmSyncRepository(app)
    }
}