package com.jefisu.manualplus.core.util

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import timber.log.Timber

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

fun FirebaseStorage.uploadFile(
    uri: Uri,
    remotePath: String,
    onFailure: (Uri?) -> Unit
) {
    var sessionUri: Uri? = null
    reference
        .child(remotePath)
        .putFile(uri)
        .addOnProgressListener { sessionUri = it.uploadSessionUri }
        .addOnFailureListener { onFailure(sessionUri) }
}

fun FirebaseStorage.retryUploadFile(
    fileBytes: ByteArray,
    remotePath: String,
    sessionUri: Uri?,
    onSuccess: () -> Unit
) {
    val imageType = remotePath.substringAfterLast(".")
    val uri = fileBytes.toUri(imageType)

    reference
        .child(remotePath)
        .putFile(uri, storageMetadata {}, sessionUri)
        .addOnSuccessListener { onSuccess() }
}