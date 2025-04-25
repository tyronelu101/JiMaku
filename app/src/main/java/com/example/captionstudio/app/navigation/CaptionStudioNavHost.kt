package com.example.captionstudio.app.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.captionstudio.audiosource.StudioModeRoute
import com.example.captionstudio.audiosource.StudioModeScreen
import com.example.captionstudio.studio.StudioRoute
import com.example.captionstudio.studio.StudioScreen
import com.example.captionstudio.transcriptions.TranscriptionsRoute
import com.example.captionstudio.transcriptions.TranscriptionsScreen

@Composable
fun CaptionStudioNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = StudioModeRoute) {
        composable<StudioModeRoute> {
            StudioModeScreen(onStudioModeClick = { mode ->
                navController.navigate(StudioRoute(mode))
            })
        }
        composable<TranscriptionsRoute> { TranscriptionsScreen() }
        composable<StudioRoute> { StudioScreen() }

    }
}