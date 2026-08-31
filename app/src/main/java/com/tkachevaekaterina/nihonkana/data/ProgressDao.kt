package com.tkachevaekaterina.nihonkana.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProgressDao {

    @Insert
    suspend fun insertExerciseSession(session: ExerciseSessionEntity): Long

    @Query("SELECT * FROM user_symbol_progress WHERE symbolId = :symbolId LIMIT 1")
    suspend fun getSymbolProgress(symbolId: Int): UserSymbolProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSymbolProgress(progress: UserSymbolProgressEntity)

    @Query("SELECT * FROM exercise_sessions ORDER BY date DESC")
    suspend fun getAllSessions(): List<ExerciseSessionEntity>

    @Query("SELECT AVG(percent) FROM exercise_sessions")
    suspend fun getAveragePercent(): Double?

    @Query("SELECT COUNT(*) FROM exercise_sessions")
    suspend fun getSessionsCount(): Int

    @Query("SELECT SUM(correctCount) FROM exercise_sessions")
    suspend fun getTotalCorrectAnswers(): Int?

    @Query("SELECT SUM(wrongCount) FROM exercise_sessions")
    suspend fun getTotalWrongAnswers(): Int?

    @Query("SELECT COUNT(*) FROM user_symbol_progress WHERE masteryPercent >= 80")
    suspend fun getLearnedSymbolsCount(): Int

    @Query("SELECT * FROM user_symbol_progress ORDER BY masteryPercent DESC")
    suspend fun getAllSymbolProgress(): List<UserSymbolProgressEntity>

    @Query("""
    SELECT 
        s.alphabet AS alphabet,
        s.rowName AS rowName,
        COUNT(s.id) AS symbolCount,
        SUM(CASE WHEN IFNULL(p.masteryPercent, 0) >= 80 THEN 1 ELSE 0 END) AS learnedCount,
        AVG(IFNULL(p.masteryPercent, 0)) AS averageMastery
    FROM symbols s
    LEFT JOIN user_symbol_progress p ON s.id = p.symbolId
    GROUP BY s.alphabet, s.rowName
    ORDER BY s.alphabet, s.rowName
""")
    suspend fun getRowProgressInfo(): List<RowProgressInfo>

    @Query("""
    SELECT 
        s.glyph AS glyph,
        s.romaji AS romaji,
        s.alphabet AS alphabet,
        s.rowName AS rowName,
        IFNULL(p.correctTotal, 0) AS correctTotal,
        IFNULL(p.wrongTotal, 0) AS wrongTotal,
        IFNULL(p.masteryPercent, 0) AS masteryPercent
    FROM symbols s
    LEFT JOIN user_symbol_progress p ON s.id = p.symbolId
    ORDER BY s.alphabet, s.rowName, s.orderIndex
""")
    suspend fun getSymbolProgressInfo(): List<SymbolProgressInfo>

    @Insert
    suspend fun insertExerciseAnswer(answer: ExerciseAnswerEntity)

    @Query("""
    SELECT * FROM exercise_answers
    WHERE symbolId = :symbolId
""")
    suspend fun getAnswersForSymbol(symbolId: Int): List<ExerciseAnswerEntity>

    @Query("""
    SELECT 
        s.glyph AS glyph,
        s.romaji AS romaji,
        COUNT(a.id) AS mistakeCount
    FROM exercise_answers a
    INNER JOIN symbols s ON a.symbolId = s.id
    WHERE a.isCorrect = 0
    GROUP BY a.symbolId
    ORDER BY mistakeCount DESC
    LIMIT 10
""")
    suspend fun getTopMistakes(): List<MistakeInfo>
}