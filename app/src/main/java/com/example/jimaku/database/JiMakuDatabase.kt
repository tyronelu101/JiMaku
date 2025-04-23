package com.example.jimaku.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jimaku.database.entities.CaptionEntity
import com.example.jimaku.database.entities.TranscriptionEntity


@Database(
    exportSchema = false, entities = [TranscriptionEntity::class, CaptionEntity::class], version = 1
)
abstract class JiMakuDatabase : RoomDatabase() {
    abstract fun transcriptionDao(): TranscriptionDao
}