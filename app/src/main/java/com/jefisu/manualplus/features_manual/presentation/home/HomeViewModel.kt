package com.jefisu.manualplus.features_manual.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.features_manual.domain.ManualRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    manualRepository: ManualRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            manualRepository.getEquipments().collect { result ->
                _state.update { state ->
                    state.copy(
                        error = result.uiText,
                        equipments = result.data.orEmpty(),
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