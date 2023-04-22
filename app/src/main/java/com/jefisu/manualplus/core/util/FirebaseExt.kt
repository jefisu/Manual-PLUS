package com.jefisu.manualplus.core.util

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata

fun fetchImageFromFirebase(remotePath: String?, onSuccess: (Uri) -> Unit) {
    if (remotePath.isNullOrBlank()) {
        return
    }
    FirebaseStorage.getInstance()
        .reference
        .child(remotePath)
        .downloadUrl
        .addOnSuccessListener {
            onSuccess(it)
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