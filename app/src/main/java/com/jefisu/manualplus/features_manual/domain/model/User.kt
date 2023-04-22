package com.jefisu.manualplus.features_manual.domain.model

data class User(
    val id: String,
    val name: String,
    val avatarRemotePath: String,
    val email: String,
    val createdAt: Long
)