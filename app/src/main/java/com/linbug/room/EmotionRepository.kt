package com.linbug.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class EmotionRepository(private val emotionDao: EmotionDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allEmotions: Flow<List<Emotion>> = emotionDao.getDateEmotions()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    fun getDateEmotion(date: String): Flow<Emotion> {
        return emotionDao.getDateEmotion(date)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteEmotion(emotion: Emotion) {
        emotionDao.deleteEmotion(emotion)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertEmotion(emotion: Emotion) {
        emotionDao.insertEmotion(emotion)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateEmotion(emotion: Emotion) {
        emotionDao.updateEmotion(emotion)
    }
}