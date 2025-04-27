package com.example.captionstudio.domain.recorder

interface AudioRecorder {
    fun record(path: String)
    fun pause()
    fun stop()
}