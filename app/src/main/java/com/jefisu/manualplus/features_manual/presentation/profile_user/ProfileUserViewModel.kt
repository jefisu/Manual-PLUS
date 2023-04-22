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
import com.jefisu.manualplus.features_manual.domain.SyncRepository
import com.jefisu.manualplus.features_manual.domain.model.ImageUpload
import com.jefisu.manualplus.features_manual.domain.model.SupportRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private var _imageUploads = mutableListOf<ImageUpload>()

    fun onEvent(event: ProfileUserEvent) {
        when (event) {
            is ProfileUserEvent.EnterName -> {
                _state.update { it.copy(name = event.value) }
            }
            is ProfileUserEvent.EnterHospitalName -> {
                _state.update { it.copy(hospitalName = event.value) }
            }
            is ProfileUserEvent.EnterHospitalAddress -> {
                _state.update { it.copy(hospitalAddress = event.value) }
            }
            is ProfileUserEvent.EnterSupportMessage -> {
                _state.update { it.copy(supportMessage = event.value) }
            }
            is ProfileUserEvent.EnterImages -> {
                _state.update { it.copy(imagesToUpload = event.value) }
            }
            is ProfileUserEvent.SelectSetting -> {
                _state.update { it.copy(settings = event.value) }
            }
            is ProfileUserEvent.Logout -> logout()
            is ProfileUserEvent.SaveChanges -> saveInfoUpdated()
            is ProfileUserEvent.SendSupportRequest -> sendSupportRequest()
        }
    }

    private fun saveInfoUpdated() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_state.value.name.isBlank()) {
                _state.update {
                    it.copy(
                        uiEvent = UiEvent.SuccessMessage(
                            UiText.StringResource(R.string.fields_can_t_be_blank)
                        )
                    )
                }
                return@launch
            }

            val result = repository.updateUserInfo(_state.value.name)
            if (result is Resource.Error) {
                _state.update { it.copy(uiEvent = UiEvent.ErrorMessage(result.uiText)) }
                return@launch
            }

            onEvent(ProfileUserEvent.EnterName(""))
            _state.update {
                it.copy(
                    name = "",
                    uiEvent = UiEvent.SuccessMessage(
                        UiText.StringResource(R.string.the_data_has_been_updated_successfully)
                    )
                )
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            app.currentUser?.logOut()
        }
    }

    private fun sendSupportRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            val areBlank = listOf(
                _state.value.hospitalName,
                _state.value.hospitalAddress,
                _state.value.supportMessage
            ).any { it.isBlank() }
            if (areBlank) {
                _state.update {
                    it.copy(
                        uiEvent = UiEvent.ErrorMessage(
                            UiText.StringResource(R.string.fields_can_t_be_blank)
                        )
                    )
                }
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
            }.also(_imageUploads::addAll)

            val result = repository.addSupportRequest(
                supportRequest.copy(remotePathImages = _imageUploads.map { it.remotePath })
            )
            if (result is Resource.Error) {
                _state.update {
                    it.copy(
                        uiEvent = UiEvent.ErrorMessage(
                            UiText.StringResource(R.string.failed_try_send_support_request)
                        )
                    )
                }
                return@launch
            }

            uploadToFirebase()
            _state.update {
                it.copy(
                    uiEvent = UiEvent.SuccessMessage(
                        UiText.StringResource(R.string.support_request_registered_successfully)
                    )
                )
            }
            _imageUploads.clear()
            _state.update {
                it.copy(
                    hospitalName = "",
                    hospitalAddress = "",
                    supportMessage = "",
                    imagesToUpload = emptyList()
                )
            }
        }
    }

    private fun uploadToFirebase() {
        val storage = FirebaseStorage.getInstance()
        _imageUploads.forEach { imageUpload ->
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
    }
}