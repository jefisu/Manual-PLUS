package com.jefisu.manualplus.features_manual.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    syncRepository: SyncRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                syncRepository.getEquipments().collect { result ->
                    _state.update { state ->
                        state.copy(
                            equipments = result.data.orEmpty(),
                            error = result.uiText,
                            categories = result.data?.map { it.category }
                                ?.distinct()
                                ?.sorted()
                                .orEmpty()
                        )
                    }
                }
            }
            launch {
                syncRepository.getUser().collect { result ->
                    _state.update { it.copy(user = result.data) }
                }
            }
        }
    }
}