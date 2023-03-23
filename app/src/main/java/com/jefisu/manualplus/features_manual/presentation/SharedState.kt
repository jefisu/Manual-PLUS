package com.jefisu.manualplus.features_manual.presentation

import android.net.Uri
import com.jefisu.manualplus.features_manual.domain.User

data class SharedState(
    val user: User? = null,
    val avatarUri: Uri = Uri.EMPTY
)