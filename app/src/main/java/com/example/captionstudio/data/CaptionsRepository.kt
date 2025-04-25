package com.example.captionstudio.data

import com.example.captionstudio.models.Caption
import kotlinx.coroutines.flow.Flow

interface CaptionsRepository {
    suspend fun insertTranscription(caption: Caption)
    fun getCaptionsByTranscription(transcriptionId: Long): Flow<List<Caption>>
    suspend fun removeTranscription(caption: Caption)
}