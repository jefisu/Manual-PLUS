package com.jefisu.manualplus.features_manual.domain.model

import android.net.Uri

data class ImageUpload(
    val uri: Uri,
    val remotePath: String
)