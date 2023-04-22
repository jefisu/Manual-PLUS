package com.jefisu.manualplus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.core.util.fetchImageFromFirebase
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import com.jefisu.manualplus.features_manual.domain.model.User
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainViewModel @Inject constructor(
    private val syncRepository: Lazy<SyncRepository>
) : ViewModel() {

    private val _user = MutableStateFlow<Pair<User?, String>>(null to "")
    val user = _user.asStateFlow()

    fun loadUser() {
        syncRepository
            .get()
            .getUser()
            .onEach { result ->
                fetchImageFromFirebase(
                    remotePath = result.data?.avatarRemotePath,
                    onSuccess = { uri ->
                        _user.update {
                            it.copy(
                                first = result.data,
                                second = uri.toString()
                            )
                        }
                    }
                )
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }
}