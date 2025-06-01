package com.example.captionstudio.domain.recorder

interface AudioRecorder {
    fun record(path: String, amplitudeListener: (amplitude: Amplitude) -> Unit)
    fun pause()
    fun stop()
}