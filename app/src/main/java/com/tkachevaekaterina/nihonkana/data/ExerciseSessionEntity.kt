package com.tkachevaekaterina.nihonkana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_sessions")
data class ExerciseSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val alphabet: String,
    val exerciseMode: String,
    val symbolGroup: String,
    val totalQuestions: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val percent: Int,
    val date: Long = System.currentTimeMillis()
)