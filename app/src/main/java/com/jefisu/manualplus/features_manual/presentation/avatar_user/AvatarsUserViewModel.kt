package com.jefisu.manualplus.features_manual.presentation.avatar_user

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URL
import java.net.URLDecoder
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AvatarsUserViewModel @Inject constructor(
    private val repository: SyncRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AvatarsUserState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("avatarUserUrl")?.let { avatar ->
            _state.update { it.copy(avatar = avatar) }
        }
        loadAvailableAvatars()
    }

    fun onEvent(event: AvatarsUserEvent) {
        when (event) {
            is AvatarsUserEvent.EnterAvatar -> {
                _state.update {
                    it.copy(avatar = event.value)
                }
            }
            is AvatarsUserEvent.ErrorDisplayed -> {
                _state.update { it.copy(error = null) }
            }
            is AvatarsUserEvent.SaveUserAvatar -> updateAvatarUser()
        }
    }

    private fun loadAvailableAvatars() {
        _state.update { it.copy(isLoading = true) }
        Firebase
            .storage
            .reference
            .child("imagens/avatar_user")
            .listAll()
            .addOnSuccessListener { listResult ->
                val uris = mutableListOf<Uri>()
                listResult.items.forEach { storageRef ->
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        uris.add(uri)
                        if (storageRef == listResult.items.last()) {
                            _state.update { state ->
                                state.copy(
                                    isLoading = false,
                                    error = null,
                                    availableAvatars = uris
                                        .map { it.toString() }
                                        .sortedBy { it.extractNumberFromString() }
                                )
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                _state.update {
                    it.copy(
                        availableAvatars = emptyList(),
                        error = UiText.unknownError(),
                        isLoading = false
                    )
                }
            }
    }

    private fun updateAvatarUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val remotePath = extractFilePathFromUrl(_state.value.avatar)
            val result = repository.updateAvatarUser(remotePath)
            if (result is Resource.Error) {
                _state.update { it.copy(uiEvent = UiEvent.ShowError(result.uiText)) }
                return@launch
            }

            _state.update { it.copy(uiEvent = UiEvent.Navigate) }
        }
    }

    private fun extractFilePathFromUrl(url: String): String {
        val urlObject = URL(url)
        val path = urlObject.path
        val index = path.lastIndexOf("/") + 1
        val encodedFilePath = path.substring(index)
        return URLDecoder.decode(encodedFilePath, "UTF-8")
    }

    private fun String.extractNumberFromString(): Int {
        val str = this
        val prefix = "Artboards_Diversity_Avatars_by_Netguru-"
        val startIndexOfNumber = str.indexOf(prefix) + prefix.length
        if (startIndexOfNumber < prefix.length) {
            return 0
        }
        val numberStr = str
            .substring(startIndexOfNumber)
            .takeWhile { it.isDigit() }
        return numberStr.toInt()
    }

    sealed class UiEvent {
        data class ShowError(val uiText: UiText?) : UiEvent()
        object Navigate : UiEvent()
    }
}