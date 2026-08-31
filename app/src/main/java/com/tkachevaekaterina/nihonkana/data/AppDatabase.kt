package com.tkachevaekaterina.nihonkana.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        KanaSymbolEntity::class,
        ExerciseSessionEntity::class,
        UserSymbolProgressEntity::class,
        ExerciseAnswerEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun kanaSymbolDao(): KanaSymbolDao
    abstract fun progressDao(): ProgressDao
}