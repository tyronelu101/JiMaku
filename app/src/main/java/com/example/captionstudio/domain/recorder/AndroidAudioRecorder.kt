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
import java.lang.Math.pow
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class AndroidAudioRecorder @Inject constructor(@ApplicationContext private val context: Context) :
    AudioRecorder {

    private val sampleRate = 44100
    private val audioEncoding = AudioFormat.ENCODING_PCM_8BIT
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        audioEncoding
    )

    private var isRecording = false
    private var recorder: AudioRecord? = null

    override fun record(path: String, amplitudeListener: (amplitude: Float) -> Unit) {
        Log.i("Test", "Buffer size is ${bufferSize}")
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

        //Byte array is signed [-128, 127]
        val audioBuffer = ByteArray(bufferSize)
        val outputFile = FileOutputStream(path)
        //A chunk is 1/4 of a second of data
        val chunk = 44100 / 4
        val segmentPerChunk = chunk / 3
        val tempBuffer = mutableListOf<Byte>()

        CoroutineScope(Dispatchers.IO).launch {
            while (isRecording) {
                val data = recorder?.read(audioBuffer, 0, bufferSize) ?: 0
                tempBuffer.addAll(audioBuffer.take(data))

                if (tempBuffer.size >= segmentPerChunk) {
                    val processedData =
                        process(
                            tempBuffer.subList(
                                0,
                                tempBuffer.size.coerceAtMost(segmentPerChunk)
                            )
                        )

                    tempBuffer.subList(0, tempBuffer.size.coerceAtMost(segmentPerChunk)).clear()
                    amplitudeListener(processedData)
                }

                if (data > 0) {
                    outputFile.write(audioBuffer)
                }
            }
            outputFile.flush()
            outputFile.close()
        }
    }

    private fun process(data: List<Byte>): Float {
        val rms = sqrt((data.sumOf {
            //Converts the signed byte to an unsigned integer
            val unsignedByte = it.toInt() and 0XFF
            //Center the value around [-128, 0, 127]
            val centeredValue = unsignedByte - 128
            centeredValue.toDouble().pow(2.0)
        }) / data.size).toFloat()
        return rms / 128f
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