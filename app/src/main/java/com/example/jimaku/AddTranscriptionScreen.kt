package com.example.jimaku

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun AddTranscriptionsRoute(
    modifier: Modifier = Modifier,
    viewModel: AddTranscriptionsViewModel = hiltViewModel()
) {


    AddTranscriptionsScreen(modifier)

}

@Composable
private fun AddTranscriptionsScreen(modifier: Modifier) {

    var text by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
            ) {
                Icon(Icons.Filled.Check, "Confirm button.")
            }
        }
    ) { padding ->
        Column(modifier = modifier.padding(padding)) {
            TextField(value = text,
                onValueChange = { text = it },
                label = { Text("Title") }
            )
            TextField(value = text,
                onValueChange = { text = it },
                label = { Text("Language") }
            )
            TextField(value = text,
                onValueChange = { text = it },
                label = { Text("Title") }
            )
        }
    }
}