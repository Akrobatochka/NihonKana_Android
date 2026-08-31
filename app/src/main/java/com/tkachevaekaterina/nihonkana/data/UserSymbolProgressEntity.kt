package com.tkachevaekaterina.nihonkana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_symbol_progress")
data class UserSymbolProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val symbolId: Int,
    val correctTotal: Int = 0,
    val wrongTotal: Int = 0,
    val lastSeenAt: Long = System.currentTimeMillis(),
    val masteryPercent: Int = 0
)