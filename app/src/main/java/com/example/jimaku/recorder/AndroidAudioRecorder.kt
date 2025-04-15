package com.example.jimaku.recorder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import javax.inject.Inject

class AndroidAudioRecorder @Inject constructor(@ApplicationContext private val context: Context) :
    AudioRecorder {
    private var isRecording = false
    private var recorder: AudioRecord? = null
    private val bufferSize = AudioRecord.getMinBufferSize(
        44123,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )


    override fun record() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44123,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        recorder?.startRecording()
        isRecording = true

        val audioBuffer = ByteArray(bufferSize)
        val outputFile = FileOutputStream("${context.filesDir}/test.pcm")
        CoroutineScope(Dispatchers.IO).launch {
            while (isRecording) {
                val data = recorder?.read(audioBuffer, 0, bufferSize) ?: 0
                Log.i("Test", "Data is ${data}")
//                if (data > 0) {
//                    outputFile.use { output ->
//                        output.write(data)
//                    }
//                }
            }
        }
    }

    override fun pause() {
        isRecording = false
    }

    override fun stop() {
        isRecording = false
        recorder?.stop()
        recorder?.release()
    }

}