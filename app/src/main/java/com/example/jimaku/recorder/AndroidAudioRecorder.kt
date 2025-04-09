package com.example.jimaku.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class AndroidAudioRecorder @Inject constructor(@ApplicationContext private val context: Context) :
    AudioRecorder {

    private var recorder: MediaRecorder? = null

    init {
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()

        }
    }

    override fun record(outputFile: String) {
        try {
            recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
            recorder?.setOutputFile(outputFile)
            recorder?.prepare()
        } catch (e: IOException) {
            Log.e(AndroidAudioRecorder::class.simpleName, "Failed to record: ${e}")
        }

        recorder?.start()

    }

    override fun pause() {
//        recorder.pause()
    }

    override fun stop() {
        recorder?.release()
        recorder = null
    }

    override fun getAmplitude(): Int = recorder?.maxAmplitude ?: -1
}