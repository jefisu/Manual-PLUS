package com.jefisu.manualplus.core.di

import com.jefisu.manualplus.features_user.presentation.data.ProfileRepositoryImpl
import com.jefisu.manualplus.features_user.presentation.domain.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.realm.kotlin.mongodb.App

@Module
@InstallIn(ViewModelComponent::class)
object ProfileModule {

    @Provides
    @ViewModelScoped
    fun provideProfileRepository(app: App): ProfileRepository {
        return ProfileRepositoryImpl(app)
    }
}