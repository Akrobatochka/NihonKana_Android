package com.tkachevaekaterina.nihonkana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "symbols")
data class KanaSymbolEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val alphabet: String,
    val rowName: String,
    val glyph: String,
    val romaji: String,
    val mnemonicText: String,
    val imageName: String,
    val orderIndex: Int,
    val audioName: String
)