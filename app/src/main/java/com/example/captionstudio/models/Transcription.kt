package com.example.captionstudio.models

import com.example.captionstudio.database.entities.TranscriptionEntity

data class Transcription(
    val name: String, val language: String, val uri: String
)

fun TranscriptionEntity.toModel(): Transcription = Transcription(
    name = this.name,
    language = this.language,
    uri = this.uri
)