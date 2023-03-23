package com.jefisu.manualplus.features_manual.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.core.util.fetchImageFromFirebase
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    syncRepository: SyncRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            syncRepository.getEquipments().collect { result ->
                result.data?.forEach { equipment ->
                    fetchImageFromFirebase(equipment.image) { uri ->
                        _state.update {
                            it.copy(equipments = it.equipments + (equipment to uri))
                        }
                    }
                }
                _state.update { state ->
                    state.copy(
                        error = result.uiText,
                        categories = result.data?.map { it.category }
                            ?.distinct()
                            ?.sorted()
                            .orEmpty()
                    )
                }
            }
        }
    }
}