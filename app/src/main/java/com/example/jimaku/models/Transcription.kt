package com.example.jimaku.models

import com.example.jimaku.database.entities.TranscriptionEntity

data class Transcription(
    val name: String, val language: String, val uri: String
)

fun TranscriptionEntity.toModel(): Transcription = Transcription(
    name = this.name,
    language = this.language,
    uri = this.uri
)