package com.tkachevaekaterina.nihonkana.data

data class RowProgressInfo(
    val alphabet: String,
    val rowName: String,
    val symbolCount: Int,
    val learnedCount: Int,
    val averageMastery: Double
)