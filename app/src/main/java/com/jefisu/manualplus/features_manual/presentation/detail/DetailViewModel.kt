package com.jefisu.manualplus.features_manual.presentation.detail

import androidx.lifecycle.SavedStateHandle
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
class DetailViewModel @Inject constructor(
    private val repository: SyncRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("id")?.let { id ->
            viewModelScope.launch {
                launch {
                    val result = repository.getEquipmentById(id)
                    _state.update {
                        it.copy(
                            equipment = result.data,
                            error = result.uiText
                        )
                    }
                }
                launch {
                    val result = repository.getConfigurationEquipment(id)
                    _state.update {
                        it.copy(
                            configurations = result.data.orEmpty(),
                            error = result.uiText
                        )
                    }
                }
            }
        }
    }
}