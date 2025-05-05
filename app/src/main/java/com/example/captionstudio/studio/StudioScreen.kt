package com.example.captionstudio.studio

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.captionstudio.R
import com.example.captionstudio.app.ui.CaptionStudioIcons
import kotlinx.serialization.Serializable

@Serializable
data class StudioRoute(val mode: StudioMode)

enum class StudioMode {
    RECORDING, AUDIO, STREAM
}

@Composable
fun StudioScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordingViewModel = hiltViewModel()
) {
    val studioUIState: StudioUIState by viewModel.studioUIState.collectAsStateWithLifecycle()
    val amplitudes: List<Float> by viewModel.amplitudes.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val audioFilePath = "${context.getExternalFilesDir(null)!!.absolutePath}/testing.pcm"
    StudioScreen(
        amplitudes,
        studioUIState,
        startRecording = { viewModel.startRecording(audioFilePath) },
        pauseRecording = { viewModel.pauseRecording() },
        onPlay = { viewModel.startPlaying(audioFilePath) },
        onPause = { viewModel.pausePlaying() },
        modifier
    )
}

@Composable
private fun StudioScreen(
    amplitudes: List<Float>,
    studioUIState: StudioUIState,
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
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        AudioWaveSeeker(
            amplitudes = amplitudes, modifier.weight(3f)
        )
        Captions(
            modifier = modifier
                .fillMaxWidth()
                .weight(4f)
        )
        StudioController(
            studioUIState = studioUIState,
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
            onDiscard = {},
            onSave = {},
            modifier = modifier
                .fillMaxWidth()
                .weight(3f)
        )
    }
}

@Composable
private fun Captions(modifier: Modifier) {
    LazyColumn(
        modifier = modifier
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
private fun StudioController(
    studioUIState: StudioUIState,
    onStartRecording: () -> Unit,
    onPauseRecording: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onDiscard: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = {}) {
            Icon(CaptionStudioIcons.CLOSE, contentDescription = stringResource(R.string.close))
        }
        RecordButton(
            isRecording = studioUIState is StudioUIState.RecordingState.Recording,
            onRecord = onStartRecording,
            onPause = onPauseRecording,
            modifier = modifier
        )
        PlaybackButton(
            isPlaying = studioUIState is StudioUIState.PlaybackState.Playing,
            onPlay = onPlay,
            onPause = onPause,
            modifier = modifier
        )
        IconButton(onClick = {}) {
            Icon(CaptionStudioIcons.CHECK, contentDescription = stringResource(R.string.pause))
        }
    }
}

@Composable
private fun RecordButton(
    isRecording: Boolean,
    onRecord: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier
) {
    Button(
        onClick = {
            if (isRecording) onPause() else onRecord()
        },
    ) {
        Text(text = if (isRecording) "Pause" else "Record")
    }
}


@Composable
private fun PlaybackButton(
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier
) {
    IconButton(onClick = {
        if (isPlaying) onPause() else onPlay()
    }) {
        Icon(
            imageVector = if (isPlaying) CaptionStudioIcons.PAUSE else CaptionStudioIcons.PLAY,
            contentDescription = if (isPlaying) stringResource(R.string.pause) else stringResource(R.string.play)
        )
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
private fun CaptionPreview() {
    Caption("1:49", "This is a caption", Modifier)
}