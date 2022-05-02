package com.linbug.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emotion_table")
data class Emotion(
    @PrimaryKey @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "emotion")val emotion: Int,
    @ColumnInfo(name = "description")val description: String?
    )