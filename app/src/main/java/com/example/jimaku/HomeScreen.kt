package com.example.jimaku

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun HomeRoute(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(onAddClick, modifier)
}

@Composable
private fun HomeScreen(onAddClick: () -> Unit, modifier: Modifier) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddClick() }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        List(modifier = modifier.padding(padding))
    }

}


@Composable
private fun List(modifier: Modifier) {

}

@Composable
fun HomeScreenPreview() {
    HomeScreen({}, modifier = Modifier)
}