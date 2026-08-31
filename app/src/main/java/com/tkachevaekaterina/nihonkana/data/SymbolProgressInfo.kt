package com.tkachevaekaterina.nihonkana.data

data class SymbolProgressInfo(
    val glyph: String,
    val romaji: String,
    val alphabet: String,
    val rowName: String,
    val correctTotal: Int,
    val wrongTotal: Int,
    val masteryPercent: Int
)