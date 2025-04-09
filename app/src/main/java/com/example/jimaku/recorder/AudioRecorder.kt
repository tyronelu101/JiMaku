package com.example.jimaku.recorder

import java.io.File
import java.net.URI

interface AudioRecorder {
    fun record(outputFile: String)
    fun pause()
    fun stop()

    fun getAmplitude(): Int
}