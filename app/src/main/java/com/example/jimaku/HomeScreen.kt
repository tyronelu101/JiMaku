package com.example.jimaku

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


}


@Composable
private fun List(modifier: Modifier) {

}

@Composable
fun HomeScreenPreview() {
    HomeScreen({}, modifier = Modifier)
}