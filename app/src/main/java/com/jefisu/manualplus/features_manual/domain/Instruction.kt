package com.jefisu.manualplus.features_manual.domain

data class Instruction(
    val id: String,
    val instructions: List<String>,
    val timeForReading: Int
)