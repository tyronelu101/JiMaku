package com.example.jimaku

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jimaku.player.AudioPlayer
import com.example.jimaku.recorder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Playback {
    PLAYING, PAUSED, STOPPED
}

data class MediaPlayerUIState(val playback: Playback)

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _isRecording: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> =
        _isRecording.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> =
        _isPlaying.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)



    fun startRecording(filePath: String) {
        _isRecording.value = true
        audioRecorder.record(filePath)
        viewModelScope.launch {
            while (_isRecording.value) {
                Log.i(
                    RecordingViewModel::class.simpleName,
                    "Amplitude is ${audioRecorder.getAmplitude()}"
                )
                delay(1000)
            }
        }

    }

    fun stopRecording() {
        _isRecording.value = false
        audioRecorder.stop()
    }

    fun startPlaying(audioPath: String) {
        _isPlaying.value = true
        audioPlayer.play(audioPath)

    }

    fun stopPlaying() {
        _isPlaying.value = false
        audioPlayer.stop()

    }
}