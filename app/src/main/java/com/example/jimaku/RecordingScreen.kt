package com.example.jimaku

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalDragOrCancellation
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RecordingScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordingViewModel = hiltViewModel()
) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("RecordingScreen", "Permission granted")
            } else {
                Log.i("RecordingScreen", "Permission denied")
            }
        }

    val isRecording: Boolean by viewModel.isRecording.collectAsStateWithLifecycle()
    val isPlaying: Boolean by viewModel.isPlaying.collectAsStateWithLifecycle()
    val amplitudes: List<Float> by viewModel.amplitudes.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxSize()) {
        Captions(modifier = modifier)
        MediaController(
            amplitudes = amplitudes,
            isRecording = isRecording,
            onStartRecording = {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) -> {
                        viewModel.startRecording("${context.externalCacheDir!!.absolutePath}/testing.mp3")
                    }

                    else -> {
                        launcher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
            },
            onStopRecording = { viewModel.stopRecording() },
            onPlay = { viewModel.startPlaying("${context.externalCacheDir!!.absolutePath}/testing.mp3") },
            modifier = modifier
        )
    }
}

@Composable
private fun Captions(modifier: Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .background(Color.Blue)
    ) {
        item {
        }
    }
}

@Composable
private fun Caption(text: String, timeStamp: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(text = text)
        Text(text = timeStamp)
    }
}

@Composable
private fun MediaController(
    isRecording: Boolean,
    amplitudes: List<Float>,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        AudioWaveFormAudioSeeker(amplitudes)
        PlaybackControl(
            isRecording,
            startRecording = onStartRecording,
            stopRecording = onStopRecording,
            onPlay = onPlay,
            modifier = modifier
        )
    }
}

@Composable
private fun AudioWaveFormAudioSeeker(amplitudes: List<Float>, modifier: Modifier = Modifier) {
    val gap = 6f.dp
    var linePosition by remember {
        mutableFloatStateOf(-1F)
    }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3F)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    val downPointerId = down.id
                    linePosition = down.position.x

                    val touchSlopChange =
                        awaitHorizontalTouchSlopOrCancellation(down.id) { change, _ ->
                            Log.i("Test", "Touch slop reached.")
                            change.consume()
                        }
                    Log.i("Test", "Touch slop is $touchSlopChange")

                    if (touchSlopChange != null) {
                        while (touchSlopChange.pressed) {
                            val drag = awaitHorizontalDragOrCancellation(downPointerId) ?: break
                            val deltaX = drag.positionChange().x
                            drag.consume()
                        }
                        Log.i("Test", "Pointer up")
                    } else {
                        Log.i("Test", "Touch slop cancelled")
                    }
                }
            }

    ) {
        drawRect(
            color = Color.Gray,
            size = size
        )
        drawLine(
            color = Color.Red,
            strokeWidth = 1.dp.toPx(),
            start = Offset(x = 0f, y = size.height / 2),
            end = Offset(size.width, size.height / 2)
        )

        var currentOffsetX = 0F
        amplitudes.forEach { amplitude ->
            val waveHeight = (amplitude / 32767f) * size.height
            val offsetY = (size.height - waveHeight) / 2

            drawRect(
                Color.White,
                topLeft = Offset(currentOffsetX, offsetY),
                size = Size(5f.dp.toPx(), waveHeight)
            )
            currentOffsetX += gap.toPx()
        }
        //Max amplitude: 32767
        if (linePosition != -1F) {
            drawLine(
                color = Color.Blue,
                strokeWidth = 3.dp.toPx(),
                start = Offset(x = linePosition, y = 0.dp.toPx()),
                end = Offset(x = linePosition, y = size.height),
            )
        }
    }
}

//Sliding wave media playback
//Ui for the captions
//Integrating whisper local library

@Composable
private fun PlaybackControl(
    isRecording: Boolean,
    startRecording: () -> Unit,
    stopRecording: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Gray)
    ) {
        IconButton(onClick = {
            if (!isRecording) {
                startRecording()
            } else {
                stopRecording()
            }
        }) {
            Icon(
                painter = painterResource(
                    id = if (!isRecording) R.drawable.baseline_mic_24 else R.drawable.baseline_mic_off_24
                ),
                contentDescription = if (!isRecording) "Start recording" else "Stop recording"
            )
        }
        IconButton(onClick = { onPlay() }) {
            Icon(
                painter = painterResource(
                    id = if (!isRecording) R.drawable.baseline_play_arrow_24 else R.drawable.baseline_pause_24
                ),
                contentDescription = if (!isRecording) "Start playing" else "Stop playing"
            )
        }

    }
}

@Preview
@Composable
fun AudioWaveFormPreview() {
    AudioWaveFormAudioSeeker(listOf(100F, 200F, 500F, 1000F))
}

@Preview
@Composable
private fun CaptionPreview() {
    Caption("This is a caption", "1:49", Modifier)
}