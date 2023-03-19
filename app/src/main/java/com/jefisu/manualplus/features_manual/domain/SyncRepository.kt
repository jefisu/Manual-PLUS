package com.jefisu.manualplus.features_manual.domain

import com.jefisu.manualplus.core.data.User
import com.jefisu.manualplus.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface SyncRepository {

    fun configureRealm()
    fun getUser(): Flow<Resource<User>>
    fun getEquipments(): Flow<Resource<List<Equipment>>>
}