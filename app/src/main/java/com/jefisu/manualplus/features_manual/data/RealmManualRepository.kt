package com.jefisu.manualplus.features_manual.data

import com.jefisu.manualplus.core.data.MongoClient
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.domain.Equipment
import com.jefisu.manualplus.features_manual.domain.ManualRepository
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class RealmManualRepository : ManualRepository {

    private val realm = MongoClient.realm

    override fun getEquipments(): Flow<Resource<List<Equipment>>> {
        return try {
            realm
                .query<EquipmentDto>()
                .asFlow()
                .map { result -> Resource.Success(result.list.map { it.toEquipment() }) }
                .flowOn(Dispatchers.IO)
        } catch (e: Exception) {
            flowOf(Resource.Error(UiText.unknownError()))
        }
    }
}