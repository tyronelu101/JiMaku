package com.example.captionstudio.studio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.captionstudio.domain.player.AudioPlayer
import com.example.captionstudio.domain.recorder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class AudioRecorderUIState {
    data object Idle : AudioRecorderUIState()
    sealed class RecordingState : AudioRecorderUIState() {
        data object Recording : RecordingState()
        data object Paused : RecordingState()
    }

    sealed class PlaybackState : AudioRecorderUIState() {
        data object Playing : PlaybackState()
        data object Paused : PlaybackState()
    }
}

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _audioRecorderUIState: MutableStateFlow<AudioRecorderUIState> =
        MutableStateFlow(AudioRecorderUIState.Idle)
    val audioRecorderUIState: StateFlow<AudioRecorderUIState> =
        _audioRecorderUIState.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AudioRecorderUIState.Idle
        )


    private val _amplitudes: MutableStateFlow<List<Float>> = MutableStateFlow(emptyList())
    val amplitudes: StateFlow<List<Float>> = _amplitudes

    fun startRecording(filePath: String) {
        _audioRecorderUIState.value = AudioRecorderUIState.RecordingState.Recording
        audioRecorder.record()
//        viewModelScope.launch {
//            while (_isRecording.value) {
////                _amplitudes.value =
////                    amplitudes.value + listOf(audioRecorder.getAmplitude().toFloat() / 32767f)
////                delay(120)
//            }
//        }
    }

    fun pauseRecording() {
        _audioRecorderUIState.value = AudioRecorderUIState.RecordingState.Paused
        audioRecorder.pause()
    }

    fun startPlaying(audioPath: String) {
        _audioRecorderUIState.value = AudioRecorderUIState.PlaybackState.Playing
        audioPlayer.play(audioPath)
    }

    fun pausePlaying() {
        _audioRecorderUIState.value = AudioRecorderUIState.PlaybackState.Paused
        audioPlayer.pause()
    }
}