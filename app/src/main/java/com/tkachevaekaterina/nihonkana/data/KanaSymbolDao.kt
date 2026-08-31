package com.tkachevaekaterina.nihonkana.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KanaSymbolDao {

    @Query("SELECT * FROM symbols WHERE alphabet = :alphabet")
    suspend fun getSymbolsByAlphabet(alphabet: String): List<KanaSymbolEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(symbols: List<KanaSymbolEntity>)

    @Query("""
        SELECT * FROM symbols
        WHERE alphabet = :alphabet AND rowName = :rowName
        ORDER BY orderIndex ASC
    """)
    suspend fun getSymbolsByAlphabetAndRow(
        alphabet: String,
        rowName: String
    ): List<KanaSymbolEntity>

    @Query("SELECT COUNT(*) FROM symbols")
    suspend fun getCount(): Int

    @Query("DELETE FROM symbols")
    suspend fun deleteAll()

}