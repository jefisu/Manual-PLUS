package com.jefisu.manualplus.features_manual.domain

data class Equipment(
    val id: String,
    val name: String,
    val image: String,
    val description: String,
    val serialNumber: Int,
    val releaseYear: Int,
    val category: String,
    val instruction: Instruction
)
