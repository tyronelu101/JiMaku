package com.example.jimaku.recorder

interface AudioRecorder {
    fun record(outputFile: String)
    fun pause()
    fun stop()
    fun getAmplitude(): Int
}