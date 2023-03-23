package com.jefisu.manualplus.core.presentation

import android.net.Uri
import com.jefisu.manualplus.core.domain.User

data class SharedState(
    val user: User? = null,
    val avatarUri: Uri = Uri.EMPTY
)
