package com.linbug.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionDao {
    @Query("SELECT * FROM emotion_table ORDER BY date ASC")
    fun getDateEmotions(): Flow<List<Emotion>>

    @Query("SELECT * FROM emotion_table WHERE date = :date ORDER BY date ASC")
    fun getDateEmotion(date: String): Flow<Emotion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmotion(emotion: Emotion)

    @Query("DELETE FROM emotion_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteEmotion(emotion: Emotion)

    @Update
    suspend fun updateEmotion(emotion: Emotion)
}