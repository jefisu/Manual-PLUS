package com.jefisu.manualplus.features_user.presentation.profile_user

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.util.ManualConstants
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.Theme
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.core.util.getThemeSystem
import com.jefisu.manualplus.features_user.presentation.domain.ProfileRepository
import com.jefisu.manualplus.features_user.presentation.profile_user.util.SettingsUser
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileUserViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val app: App,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUserState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentTheme = getThemeSystem(prefs)

    fun enteredName(value: String) {
        _state.update { it.copy(name = value) }
    }

    fun selectTheme(theme: Theme) {
        _state.update { it.copy(theme = theme) }
    }

    fun selectSetting(settings: SettingsUser) {
        _state.update { it.copy(settings = settings) }
    }

    fun resetOptionThemeSelected() {
        if (state.value.theme != currentTheme) {
            _state.update { it.copy(theme = currentTheme) }
        }
    }

    fun saveTheme() {
        prefs.edit()
            .putString(ManualConstants.THEME_ID, _state.value.theme.name)
            .apply()
        currentTheme = _state.value.theme
        viewModelScope.launch {
            _uiEvent.send(UiEvent.HideBottomSheet)
        }
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

    sealed class UiEvent {
        data class ErrorMessage(val uiText: UiText?) : UiEvent()
        data class SuccessMessage(val uiText: UiText?) : UiEvent()
        object HideBottomSheet : UiEvent()
    }
}