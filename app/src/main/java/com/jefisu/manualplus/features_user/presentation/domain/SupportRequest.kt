package com.jefisu.manualplus.features_user.presentation.domain

import org.mongodb.kbson.BsonObjectId

data class SupportRequest(
    val id: BsonObjectId = BsonObjectId(),
    val hospitalName: String,
    val hospitalAddress: String,
    val problem: String,
    val remotePathImages: List<String>
)