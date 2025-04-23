package com.example.jimaku.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TranscriptionEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("transcriptionId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CaptionEntity(
    @PrimaryKey(autoGenerate = true)
    val captionID: Long = 0L,
    val transcriptionId: Long,
    val timeStampStart: Long,
    val timeStampEnd: Long,
    val text: String
)