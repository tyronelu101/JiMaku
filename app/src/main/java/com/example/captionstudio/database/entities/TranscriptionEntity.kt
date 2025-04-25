package com.example.captionstudio.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TranscriptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val language: String,
    val uri: String
)

