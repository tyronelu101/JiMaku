package com.example.captionstudio.data

import com.example.captionstudio.models.Transcription

interface TranscriptionRepository {
    suspend fun insertTranscription(transcription: Transcription)
    suspend fun getTranscription(): Transcription
    suspend fun removeTranscription(transcription: Transcription)
}
