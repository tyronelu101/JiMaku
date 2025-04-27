package com.example.captionstudio.domain.recorder

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

    private val sampleRate = 44100
    private val audioEncoding = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        audioEncoding
    )

    private var isRecording = false
    private var recorder: AudioRecord? = null

    override fun record(filePath: String) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            audioEncoding,
            bufferSize
        )

        recorder?.startRecording()
        isRecording = true

        val audioBuffer = ByteArray(bufferSize * 2)
        val outputFile = FileOutputStream(filePath)
        CoroutineScope(Dispatchers.IO).launch {
            while (isRecording) {
                val data = recorder?.read(audioBuffer, 0, bufferSize * 2) ?: 0
                Log.i("Test", "Writing out to file ${data}")
                if (data > 0) {
                    outputFile.write(audioBuffer)
                }
            }
            outputFile.flush()
            outputFile.close()
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