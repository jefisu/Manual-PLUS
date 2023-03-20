package com.jefisu.manualplus.features_manual.domain

import com.jefisu.manualplus.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface ManualRepository {
    fun getEquipments(): Flow<Resource<List<Equipment>>>
}