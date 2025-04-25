package com.example.captionstudio.studio

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.captionstudio.R
import kotlinx.serialization.Serializable

@Serializable
data class StudioRoute(val mode: StudioMode)

fun NavController.navigateToRecording(navOptions: NavOptions) =
    navigate(route = StudioRoute, navOptions)

enum class StudioMode {
    RECORDING, AUDIO, STREAM
}

@Composable
fun StudioScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordingViewModel = hiltViewModel()
) {
    val audioRecorderUIState: AudioRecorderUIState by viewModel.audioRecorderUIState.collectAsStateWithLifecycle()
    val amplitudes: List<Float> by viewModel.amplitudes.collectAsStateWithLifecycle()
    val context = LocalContext.current
    StudioScreen(
        amplitudes,
        audioRecorderUIState,
        startRecording = { viewModel.startRecording("${context.externalCacheDir!!.absolutePath}/testing.mp3") },
        pauseRecording = { viewModel.pauseRecording() },
        onPlay = { viewModel.startPlaying("${context.externalCacheDir!!.absolutePath}/testing.mp3}") },
        onPause = { viewModel.pausePlaying() },
        modifier
    )
}

@Composable
private fun StudioScreen(
    amplitudes: List<Float>,
    audioRecorderUIState: AudioRecorderUIState,
    startRecording: () -> Unit,
    pauseRecording: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier
) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("RecordingScreen", "Permission granted")
            } else {
                Log.i("RecordingScreen", "Permission denied")
            }
        }

    val context = LocalContext.current
    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxSize()) {

        AudioWaveFormAudioSeeker(amplitudes = amplitudes)
        Captions(modifier = modifier)
        MediaController(
            audioRecorderUIState = audioRecorderUIState,
            onStartRecording = {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) -> {
                        startRecording()
                    }

                    else -> {
                        launcher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
            },
            onPauseRecording = { pauseRecording() },
            onPlay = { onPlay() },
            onPause = { onPause() },
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
private fun Caption(timeStamp: String, text: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(text = timeStamp)
        Text(text = text)
    }
}

@Composable
private fun MediaController(
    audioRecorderUIState: AudioRecorderUIState,
    onStartRecording: () -> Unit,
    onPauseRecording: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        PlaybackControl(
            audioRecorderUIState,
            startRecording = onStartRecording,
            pauseRecording = onPauseRecording,
            playAudio = onPlay,
            pauseAudio = onPause,
            modifier = modifier
        )
    }
}

@Composable
private fun AudioWaveFormAudioSeeker(amplitudes: List<Float>, modifier: Modifier = Modifier) {
    val waveWidth = 2f
    val gap = 5f
    var verticalLinePosition by remember {
        mutableFloatStateOf(-1F)
    }
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .background(Color.Red)
            .fillMaxHeight(0.3F)
            .fillMaxWidth()
            .horizontalScroll(scrollState)
    ) {
        Canvas(
            modifier = Modifier
                .background(Color.Green)
                .width((amplitudes.size * (gap + waveWidth)).dp)
                .fillMaxHeight()
//                .pointerInput(Unit) {
//                    awaitEachGesture {
//                        val down = awaitFirstDown()
//                        val downPointerId = down.id
//                        linePosition = down.position.x
//
//                        val touchSlopChange =
//                            awaitHorizontalTouchSlopOrCancellation(down.id) { change, _ ->
//                                Log.i("Test", "Touch slop reached.")
//                                change.consume()
//                            }
//                        Log.i("Test", "Touch slop is $touchSlopChange")
//
//                        if (touchSlopChange != null) {
//                            while (touchSlopChange.pressed) {
//                                val drag = awaitHorizontalDragOrCancellation(downPointerId) ?: break
//                                val deltaX = drag.positionChange().x
//                                drag.consume()
//                            }
//                            Log.i("Test", "Pointer up")
//                        } else {
//                            Log.i("Test", "Touch slop cancelled")
//                        }
//                    }
//                }

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
                val waveHeight = amplitude * size.height
                val offsetY = (size.height - waveHeight) / 2

                drawRect(
                    Color.White,
                    topLeft = Offset(currentOffsetX, offsetY),
                    size = Size(waveWidth, waveHeight)
                )
                currentOffsetX += gap.dp.toPx()
            }
            //Max amplitude: 32767
            if (verticalLinePosition != -1F) {
                drawLine(
                    color = Color.Blue,
                    strokeWidth = 3.dp.toPx(),
                    start = Offset(x = verticalLinePosition, y = 0.dp.toPx()),
                    end = Offset(x = verticalLinePosition, y = size.height),
                )
            }
        }
    }
}

@Composable
private fun PlaybackControl(
    audioRecorderUIState: AudioRecorderUIState,
    startRecording: () -> Unit,
    pauseRecording: () -> Unit,
    playAudio: () -> Unit,
    pauseAudio: () -> Unit,
    modifier: Modifier
) {
    Log.i("Test", "UI state is ${audioRecorderUIState}")
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Gray)
    ) {

        when (audioRecorderUIState) {
            AudioRecorderUIState.Idle -> {
                AudioControlButton(
                    R.drawable.baseline_mic_24,
                    "Start Recording",
                    startRecording,
                    modifier
                )
            }

            AudioRecorderUIState.RecordingState.Recording -> {
                AudioControlButton(
                    R.drawable.baseline_mic_off_24,
                    "Pause Recording",
                    pauseRecording,
                    modifier
                )
            }

            AudioRecorderUIState.RecordingState.Paused -> {
                AudioControlButton(
                    R.drawable.baseline_mic_24,
                    "Start Recording",
                    startRecording,
                    modifier
                )
                AudioControlButton(
                    R.drawable.baseline_play_arrow_24,
                    "Play audio",
                    playAudio,
                    modifier
                )
            }

            AudioRecorderUIState.PlaybackState.Playing -> {
                AudioControlButton(
                    R.drawable.baseline_pause_24,
                    "Pause audio",
                    pauseAudio,
                    modifier
                )
            }

            AudioRecorderUIState.PlaybackState.Paused -> {
                AudioControlButton(
                    R.drawable.baseline_mic_24,
                    "Start recording",
                    startRecording,
                    modifier
                )
                AudioControlButton(
                    R.drawable.baseline_play_arrow_24,
                    "Play audio",
                    playAudio,
                    modifier
                )
            }
        }
    }
}


@Composable
private fun AudioControlButton(
    iconResource: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(onClick = { onClick() }) {
        Icon(
            painter = painterResource(
                id = iconResource
            ),
            contentDescription = contentDescription
        )
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
    Caption("1:49", "This is a caption", Modifier)
}