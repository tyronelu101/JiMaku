package com.example.jimaku.player

interface AudioPlayer {
    fun play(audioPath: String)
    fun pause()
    fun stop()
    fun seek(time: Int)

    fun setOnProgressListener(listener: AudioPlayerListener)
}

interface AudioPlayerListener {
    fun onProgress(time: Int)
}