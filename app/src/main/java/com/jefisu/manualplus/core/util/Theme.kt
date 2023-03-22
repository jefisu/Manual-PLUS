package com.jefisu.manualplus.core.util

import android.content.SharedPreferences
import androidx.annotation.StringRes
import com.jefisu.manualplus.R

enum class Theme(@StringRes val res: Int) {
    SystemDefault(R.string.systemDefault),
    Light(R.string.light),
    Dark(R.string.dark),
}

fun getThemeSystem(prefs: SharedPreferences): Theme {
    val themeValue = prefs.getString(ManualConstants.THEME_ID, null)
    return if (themeValue == null) {
        Theme.SystemDefault
    } else {
        Theme.valueOf(themeValue)
    }
}