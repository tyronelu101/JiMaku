package com.example.captionstudio.domain.player

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.AudioTrack.MODE_STREAM
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileInputStream
import javax.inject.Inject

class AndroidAudioPlayer @Inject constructor() : AudioPlayer {

    private var audioPlayer: AudioTrack? = null
    private var listener: AudioPlayerListener? = null

    private val buffer = AudioTrack.getMinBufferSize(
        44100,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    init {
        val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        val audioFormat =
            AudioFormat.Builder().setSampleRate(44100).setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT).build()
        audioPlayer = AudioTrack(
            audioAttributes, audioFormat,
            buffer, MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE
        )
    }

    override fun play(audioPath: String) {
        val fileInputStream = FileInputStream(audioPath)
        val pcmData = ByteArray(fileInputStream.available())
        fileInputStream.read(pcmData)
        fileInputStream.close()

        audioPlayer?.play()
        var offset = 0
        var chunkSize: Int = buffer
        CoroutineScope(Dispatchers.IO).launch {
            while (offset < pcmData.size) {
                chunkSize = Math.min(chunkSize, pcmData.size - offset)
                audioPlayer?.write(pcmData, offset, chunkSize)
                offset += chunkSize
            }
            Log.i("Test", "Audio playback complete")
        }
    }

    override fun pause() {
        audioPlayer?.pause()
    }

    override fun stop() {
        audioPlayer?.stop()
        audioPlayer?.release()
        audioPlayer = null
        listener = null
    }

    override fun seek(time: Int) {
//        audioPlayer?.seekTo(time)
    }

    override fun setOnProgressListener(listener: AudioPlayerListener) {
        this.listener = listener
    }
}