package com.example.jimaku

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController


@Composable
fun App(modifier: Modifier) {
    val navController = rememberNavController()
    JiMakuNavHost(navController = navController)
}