package com.jefisu.manualplus.core.presentation

import android.net.Uri
import com.jefisu.manualplus.core.domain.User
import com.jefisu.manualplus.core.util.Theme

data class SharedState(
    val user: User? = null,
    val avatarUri: Uri? = null,
    val theme: Theme = Theme.SystemDefault
)
