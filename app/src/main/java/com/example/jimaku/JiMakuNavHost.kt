package com.example.jimaku

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jimaku.models.Transcription
import com.example.jimaku.recording.RecordingScreen

@Serializable
data object Home

@Serializable
data object Record

@Serializable
data object AddTranscription

@Composable
fun JiMakuNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> { HomeRoute(onAddClick = { navController.navigate(AddTranscription) }) }
        composable<Record> { RecordingScreen() }
        composable<AddTranscription> { AddTranscriptionsRoute() }
    }
}