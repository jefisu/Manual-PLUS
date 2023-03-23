package com.jefisu.manualplus.features_manual.presentation.profile_user

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.data.entity.FileToUploadEntity
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.core.util.uploadFile
import com.jefisu.manualplus.features_manual.domain.ImageUpload
import com.jefisu.manualplus.features_manual.domain.SupportRequest
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import com.jefisu.manualplus.features_manual.presentation.profile_user.util.SettingsUser
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileUserViewModel @Inject constructor(
    private val app: App,
    private val repository: SyncRepository,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUserState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var imageUploads = mutableListOf<ImageUpload>()

    fun enteredName(value: String) {
        _state.update { it.copy(name = value) }
    }

    fun enteredHospitalName(value: String) {
        _state.update { it.copy(hospitalName = value) }
    }

    fun enteredHospitalAddress(value: String) {
        _state.update { it.copy(hospitalAddress = value) }
    }

    fun enteredSupportMessage(value: String) {
        _state.update { it.copy(supportMessage = value) }
    }

    fun selectSetting(settings: SettingsUser) {
        _state.update { it.copy(settings = settings) }
    }

    fun selectedImagesToUpload(uris: List<Uri>) {
        _state.update { it.copy(imagesToUpload = it.imagesToUpload + uris) }
    }

    fun saveInfoUpdated() {
        viewModelScope.launch {
            if (_state.value.name.isBlank()) {
                _uiEvent.send(
                    UiEvent.ErrorMessage(UiText.StringResource(R.string.fields_can_t_be_blank))
                )
                return@launch
            }

            val result = repository.updateUserInfo(_state.value.name)
            when (result) {
                is Resource.Error -> _uiEvent.send(UiEvent.ErrorMessage(result.uiText))
                is Resource.Success -> {
                    enteredName("")
                    _uiEvent.send(UiEvent.HideBottomSheet)
                    _uiEvent.send(
                        UiEvent.SuccessMessage(UiText.StringResource(R.string.the_data_has_been_updated_successfully))
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            app.currentUser?.logOut()
        }
    }

    fun sendSupportRequest() {
        viewModelScope.launch {
            val areBlank = listOf(
                _state.value.hospitalName,
                _state.value.hospitalAddress,
                _state.value.supportMessage
            ).any { it.isBlank() }
            if (areBlank) {
                _uiEvent.send(UiEvent.ErrorMessage(UiText.StringResource(R.string.fields_can_t_be_blank)))
                return@launch
            }

            val supportRequest = SupportRequest(
                hospitalName = _state.value.hospitalName,
                hospitalAddress = _state.value.hospitalAddress,
                problem = _state.value.supportMessage,
                remotePathImages = emptyList()
            )

            _state.value.imagesToUpload.map { uri ->
                val imageType = uri.extractImageType()
                val remotePath =
                    "imagens/support_request/${supportRequest.id.toHexString()}/${UUID.randomUUID()}.$imageType"
                ImageUpload(uri, remotePath)
            }.also(imageUploads::addAll)

            val result = repository.addSupportRequest(
                supportRequest.copy(remotePathImages = imageUploads.map { it.remotePath })
            )
            when (result) {
                is Resource.Error -> {
                    _uiEvent.send(
                        UiEvent.ErrorMessage(UiText.StringResource(R.string.failed_try_send_support_request))
                    )
                }
                is Resource.Success -> {
                    uploadToFirebase()
                    _uiEvent.send(UiEvent.HideBottomSheet)
                    _uiEvent.send(
                        UiEvent.SuccessMessage(UiText.StringResource(R.string.support_request_registered_successfully))
                    )
                    imageUploads.clear()
                    enteredHospitalName("")
                    enteredHospitalAddress("")
                    enteredSupportMessage("")
                    selectedImagesToUpload(emptyList())
                }
            }
        }
    }

    private fun uploadToFirebase() {
        val storage = FirebaseStorage.getInstance()
        imageUploads.forEach { imageUpload ->
            storage.uploadFile(
                uri = imageUpload.uri,
                remotePath = imageUpload.remotePath,
                onFailure = { sessionUri ->
                    val inputStream = application.contentResolver.openInputStream(imageUpload.uri)
                    viewModelScope.launch {
                        FileToUploadEntity(
                            remotePath = imageUpload.remotePath,
                            fileBytes = inputStream!!.readBytes(),
                            sessionUri = sessionUri.toString()
                        ).also { repository::addFileToUpload }
                    }
                    inputStream?.close()
                }
            )
        }
    }

    private fun Uri.extractImageType(): String {
        return application.contentResolver
            .getType(this)
            ?.substringAfter("/") ?: "jpg"
    }

    sealed class UiEvent {
        data class ErrorMessage(val uiText: UiText?) : UiEvent()
        data class SuccessMessage(val uiText: UiText?) : UiEvent()
        object HideBottomSheet : UiEvent()
    }
}