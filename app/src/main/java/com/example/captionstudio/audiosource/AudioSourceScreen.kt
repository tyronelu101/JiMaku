package com.example.captionstudio.audiosource

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.captionstudio.studio.StudioMode
import kotlinx.serialization.Serializable

@Serializable
data object StudioModeRoute

@Composable
fun StudioModeScreen(
    onStudioModeClick: (studioMode: StudioMode) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AudioSourceViewModel = hiltViewModel()
) {
    StudioModeScreen(onStudioModeClick, modifier)
}

@Composable
private fun StudioModeScreen(
    onStudioModeClick: (studioMode: StudioMode) -> Unit,
    modifier: Modifier
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Button(onClick = { onStudioModeClick(StudioMode.RECORDING) }) { Text("Record") }
        Button(onClick = { onStudioModeClick(StudioMode.AUDIO) }) { Text("Audio") }
        Button(onClick = { onStudioModeClick(StudioMode.STREAM) }) { Text("Stream") }
    }

}

@Composable
fun HomeScreenPreview() {
    StudioModeScreen({}, modifier = Modifier)
}