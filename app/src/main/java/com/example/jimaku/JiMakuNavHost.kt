package com.example.jimaku

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jimaku.recording.RecordingScreen


@Serializable
data object Record


@Composable
fun JiMakuNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Record) {
        composable<Record> { RecordingScreen() }
    }
}