package com.jefisu.manualplus.core.domain

import com.jefisu.manualplus.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface SharedRepository {
    fun getUser(): Flow<Resource<User>>
}