package com.jefisu.manualplus.core.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.jefisu.manualplus.core.util.Theme
import com.jefisu.manualplus.core.util.getThemeSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    prefs: SharedPreferences
) : ViewModel() {

    private val _theme = MutableStateFlow(Theme.SystemDefault)
    val theme = _theme.asStateFlow()

    init {
        selectTheme(getThemeSystem(prefs))
    }

    fun selectTheme(theme: Theme) {
        _theme.update { theme }
    }
}