package com.jefisu.manualplus.core.util

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber

sealed class FirebaseResponse {
    object Success : FirebaseResponse()
    object Failure : FirebaseResponse()
}

fun fetchImageFromFirebase(remotePath: String?, response: (Uri?) -> Unit) {
    if (remotePath == null) {
        Timber.d("Invalid remote path")
        response(null)
        return
    }
    FirebaseStorage.getInstance()
        .reference
        .child(remotePath)
        .downloadUrl
        .addOnSuccessListener {
            Timber.d("downloadURL: $it")
            response(it)
        }
        .addOnFailureListener {
            Timber.d("downloadURL error: ${it.message}")
        }
}