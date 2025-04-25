package com.example.captionstudio.data

import com.example.captionstudio.database.TranscriptionDao
import com.example.captionstudio.models.Transcription
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