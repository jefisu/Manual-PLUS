package com.jefisu.manualplus.features_manual.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import com.jefisu.manualplus.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: SyncRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    init {
        val navArgs = savedStateHandle.navArgs<DetailNavArgs>()
        repository.getEquipmentById(navArgs.id)
            .onEach { result ->
                _state.update {
                    it.copy(
                        equipment = result.data,
                        error = result.uiText,
                        isLoading = false
                    )
                }
            }
            .onStart {
                _state.update {
                    it.copy(
                        imageUrl = navArgs.imageUrl,
                        isLoading = true
                    )
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }
}