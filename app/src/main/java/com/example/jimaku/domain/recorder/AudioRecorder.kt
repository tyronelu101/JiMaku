package com.example.jimaku.domain.recorder

interface AudioRecorder {
    fun record()
    fun pause()
    fun stop()
}