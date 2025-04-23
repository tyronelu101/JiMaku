package com.example.jimaku

import com.example.jimaku.database.TranscriptionDao
import com.example.jimaku.models.Transcription
import javax.inject.Inject

class LocalTranscriptionsRepo @Inject constructor(private val transcriptionsDao: TranscriptionDao) :
    TranscriptionRepository {
    override suspend fun insertTranscription(transcription: Transcription) {
        TODO("Not yet implemented")
    }

    override suspend fun getTranscription(): Transcription {
        TODO("Not yet implemented")
    }

    override suspend fun removeTranscription(transcription: Transcription) {
        TODO("Not yet implemented")
    }
}