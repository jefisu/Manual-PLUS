package com.jefisu.manualplus.features_user.presentation.domain

import android.net.Uri

data class ImageUpload(
    val uri: Uri,
    val remotePath: String
)