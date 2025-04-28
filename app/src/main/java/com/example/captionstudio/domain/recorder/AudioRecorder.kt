package com.example.captionstudio.domain.recorder

interface AudioRecorder {
    fun record(path: String, amplitudeListener: (amplitude: Float) -> Unit)
    fun pause()
    fun stop()
}