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
import kotlinx.serialization.Serializable

@Serializable
data object AudioRoute

@Composable
fun AudioSourceScreen(
    modifier: Modifier = Modifier,
    viewModel: AudioSourceViewModel = hiltViewModel()
) {
    AudioSourceScreen(modifier)
}

@Composable
private fun AudioSourceScreen(modifier: Modifier) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Button(onClick = {}) { Text("Record") }
        Button(onClick = {}) { Text("Audio") }
    }

}

@Composable
fun HomeScreenPreview() {
    AudioSourceScreen(modifier = Modifier)
}