package com.jefisu.manualplus.features_manual.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.core.util.fetchImageFromFirebase
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: Lazy<SyncRepository>
) : ViewModel() {

    private val _state = MutableStateFlow(SharedState())
    val state = _state.asStateFlow()

    fun getUser() {
        repository
            .get()
            .getUser().onEach { result ->
                fetchImageFromFirebase(
                    remotePath = result.data?.avatarRemotePath,
                    response = { uri ->
                        _state.update {
                            it.copy(user = result.data, avatarUri = uri)
                        }
                    }
                )
            }.launchIn(viewModelScope)
    }
}