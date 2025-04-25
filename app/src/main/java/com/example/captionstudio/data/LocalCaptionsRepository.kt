package com.example.captionstudio.data

import com.example.captionstudio.models.Caption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalCaptionsRepository : CaptionsRepository {
    override suspend fun insertTranscription(caption: Caption) {

    }

    override fun getCaptionsByTranscription(transcriptionId: Long): Flow<List<Caption>> {
        return flow {}
    }

    override suspend fun removeTranscription(caption: Caption) {

    }
}