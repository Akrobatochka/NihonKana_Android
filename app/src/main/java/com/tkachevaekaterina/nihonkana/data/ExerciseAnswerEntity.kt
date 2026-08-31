package com.tkachevaekaterina.nihonkana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_answers")
data class ExerciseAnswerEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sessionId: Int,
    val symbolId: Int,
    val selectedAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val answeredAt: Long = System.currentTimeMillis()
)