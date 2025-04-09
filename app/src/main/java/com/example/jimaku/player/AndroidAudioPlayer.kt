package com.example.jimaku.player

import android.media.MediaPlayer
import android.util.Log
import java.io.IOException
import javax.inject.Inject

class AndroidAudioPlayer @Inject constructor() : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var listener: AudioPlayerListener? = null

    init {
        mediaPlayer = MediaPlayer()
    }

    override fun play(audioPath: String) {
        try {

            mediaPlayer?.setDataSource(audioPath)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        } catch (e: IOException) {
            Log.e(AndroidAudioPlayer::class.simpleName, "Failed to play audio: $e")
        }
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        listener = null
    }

    override fun seek(time: Int) {
        mediaPlayer?.seekTo(time)
    }

    override fun setOnProgressListener(listener: AudioPlayerListener) {
        this.listener = listener
    }
}