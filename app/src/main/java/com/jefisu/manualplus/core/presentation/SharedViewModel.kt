package com.jefisu.manualplus.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.core.domain.SharedRepository
import com.jefisu.manualplus.core.util.fetchImageFromFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    repository: SharedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SharedState())
    val state = _state.asStateFlow()

    init {
        repository.getUser().onEach { result ->
            fetchImageFromFirebase(
                remotePath = result.data?.avatarRemotePath,
                response = { uri ->
                    _state.update {
                        it.copy(
                            user = result.data,
                            avatarUri = uri
                        )
                    }
                }
            )
        }.launchIn(viewModelScope)
    }
}