package com.example.captionstudio.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.captionstudio.database.entities.CaptionEntity
import com.example.captionstudio.database.entities.TranscriptionEntity


@Database(
    exportSchema = false, entities = [TranscriptionEntity::class, CaptionEntity::class], version = 1
)
abstract class CaptionStudioDatabase : RoomDatabase() {
    abstract fun transcriptionDao(): TranscriptionDao
}