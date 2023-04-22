package com.jefisu.manualplus.core.connectivity

import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.util.UiText
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>

    sealed class Status(val uiText: UiText? = null) {
        object Available : Status()
        object Unavailable : Status(internetUnavailable())
    }

    companion object {
        fun internetUnavailable(): UiText {
            return UiText.StringResource(R.string.you_re_offline_check_your_internet_connection)
        }
    }
}