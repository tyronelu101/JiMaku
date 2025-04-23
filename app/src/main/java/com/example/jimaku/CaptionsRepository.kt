package com.example.jimaku

import com.example.jimaku.models.Caption
import kotlinx.coroutines.flow.Flow

interface CaptionsRepository {
    suspend fun insertTranscription(caption: Caption)
    fun getCaptionsByTranscription(transcriptionId: Long): Flow<List<Caption>>
    suspend fun removeTranscription(caption: Caption)
}