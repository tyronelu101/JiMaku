package com.example.jimaku

import com.example.jimaku.models.Caption
import com.example.jimaku.models.Transcription

interface TranscriptionRepository {
    suspend fun insertTranscription(transcription: Transcription)
    suspend fun getTranscription(): Transcription
    suspend fun removeTranscription(transcription: Transcription)
}
