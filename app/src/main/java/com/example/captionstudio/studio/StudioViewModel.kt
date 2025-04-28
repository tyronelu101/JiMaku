package com.example.captionstudio.studio

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.captionstudio.domain.player.AudioPlayer
import com.example.captionstudio.domain.recorder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

sealed interface StudioUIState {
    data object Idle : StudioUIState
    sealed class RecordingState : StudioUIState {
        data object Recording : RecordingState()
        data object Paused : RecordingState()
    }

    sealed class PlaybackState : StudioUIState {
        data object Playing : PlaybackState()
        data object Paused : PlaybackState()
    }
}

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val studioParams = savedStateHandle.toRoute<StudioRoute>()

    private val _studioUIState: MutableStateFlow<StudioUIState> =
        MutableStateFlow(StudioUIState.Idle)
    val studioUIState: StateFlow<StudioUIState> =
        _studioUIState.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            StudioUIState.Idle
        )

    private val _amplitudes: MutableStateFlow<List<Float>> = MutableStateFlow(emptyList())
    val amplitudes: StateFlow<List<Float>> = _amplitudes

    fun startRecording(filePath: String) {
        Log.i("Test", "Starting recording")
        _studioUIState.value = StudioUIState.RecordingState.Recording
        audioRecorder.record(filePath, {})
        viewModelScope.launch(Dispatchers.IO) {
            while (studioUIState.value is StudioUIState.RecordingState.Recording) {
                val amplitude = Math.round((Random.nextFloat() * 9) + 1) / 10f
                _amplitudes.value = amplitudes.value + listOf(amplitude)
                delay(60)
            }
        }
    }

    fun pauseRecording() {
        _studioUIState.value = StudioUIState.RecordingState.Paused
        audioRecorder.pause()
    }

    fun startPlaying(audioPath: String) {
        _studioUIState.value = StudioUIState.PlaybackState.Playing
        audioPlayer.play(audioPath)
    }

    fun pausePlaying() {
        _studioUIState.value = StudioUIState.PlaybackState.Paused
        audioPlayer.pause()
    }
}