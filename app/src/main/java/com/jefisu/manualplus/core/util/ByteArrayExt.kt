package com.jefisu.manualplus.core.util

import android.net.Uri
import android.util.Base64

fun ByteArray.toUri(fileType: String = "png"): Uri {
    val encodedString = Base64.encodeToString(this, Base64.DEFAULT)
    return Uri.parse("data:image/$fileType;base64,$encodedString")
}
