package com.jefisu.manualplus.features_manual.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    syncRepository: SyncRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .combine(syncRepository.getEquipments()) { state, resultEquipments ->
            state.copy(
                isLoading = false,
                error = resultEquipments.uiText,
                equipments = resultEquipments.data.orEmpty(),
                categories = resultEquipments.data?.map { it.category }
                    ?.distinct()
                    ?.sorted()
                    .orEmpty()
            )
        }
        .onStart { _state.update { it.copy(isLoading = true) } }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value
        )
}