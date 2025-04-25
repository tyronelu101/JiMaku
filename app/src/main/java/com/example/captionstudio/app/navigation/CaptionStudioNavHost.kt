package com.example.captionstudio.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.captionstudio.audiosource.AudioRoute
import com.example.captionstudio.audiosource.AudioSourceScreen
import com.example.captionstudio.transcriptions.TranscriptionsRoute
import com.example.captionstudio.transcriptions.TranscriptionsScreen

@Composable
fun CaptionStudioNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AudioRoute) {
        composable<AudioRoute> { AudioSourceScreen() }
        composable<TranscriptionsRoute> { TranscriptionsScreen() }
    }
}