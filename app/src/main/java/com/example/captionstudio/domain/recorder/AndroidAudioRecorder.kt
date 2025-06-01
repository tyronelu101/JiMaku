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
    private val audioEncoding = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        audioEncoding
    )

    private var isRecording = false
    private var recorder: AudioRecord? = null

    //sample rate * channel(mono-1, stereo-2), * (8 bit encoding-1, 16bit-2)
    private val bytesPerSecond = sampleRate * 1 * 2

    //A chunk is 1/4 of a second of data
    private val chunk = bytesPerSecond / 4
    private val segmentPerChunk = chunk / 3

    override fun record(path: String, amplitudeListener: (amplitude: Float) -> Unit) {
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

        //Byte array is signed [-32768, 32767]
        val audioBuffer = ByteArray(bufferSize)
        val outputFile = FileOutputStream(path)

        val tempBuffer = mutableListOf<Byte>()

        CoroutineScope(Dispatchers.IO).launch {
            while (isRecording) {
                val data = recorder?.read(audioBuffer, 0, bufferSize) ?: 0
                tempBuffer.addAll(audioBuffer.take(data))
                //Each element in buffer is 1 byte. Audio is encoded in 2bytes.
                if (tempBuffer.size >= segmentPerChunk) {
                    val processedData =
                        process(
                            tempBuffer.subList(
                                0,
                                tempBuffer.size.coerceAtMost(segmentPerChunk)
                            )
                        )

                    tempBuffer.subList(0, tempBuffer.size.coerceAtMost(segmentPerChunk)).clear()
                    Log.i("Test", "Data is ${processedData}")
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

    //List of byte represents one sound bar
    //16bit and little endian
    private fun process(data: List<Byte>): Float {
        var sum = 0f
        val sampleCount = data.size / 2

        for (i in data.indices step 2) {
            //Need to combine data[i and i+1] to get 16bits
            //Converts the signed byte to an unsigned integer
            val leastSigBytes = (data[i].toInt() and 0XFF)
            val mostSigBytes = data[i + 1].toInt() shl 8
            val sample = (mostSigBytes or leastSigBytes).toShort().toInt()
            sum += sample.toDouble().pow(2.0).toFloat()
        }
        val rms = sqrt(sum / sampleCount)
        return rms / 32768
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