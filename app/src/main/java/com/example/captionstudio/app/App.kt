package com.example.captionstudio.app

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.captionstudio.app.navigation.CaptionStudioNavHost
import com.example.captionstudio.app.navigation.TopLevelDestinations


@Composable
fun App(appState: AppState, modifier: Modifier = Modifier) {

    val currentDestination = appState.currentDestination
    Log.i("Test", "Current destination is ${currentDestination}")
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TopLevelDestinations.entries.forEach { topLevelDestination ->
//                val isSelected = currentDestination.hierarchy.any{it.hasRoute(topLevelDestination.route)}
                item(
                    icon = {
                        Icon(
                            topLevelDestination.icon,
                            contentDescription = stringResource(id = topLevelDestination.contentDescription)
                        )
                    },
                    label = { Text(text = stringResource(id = topLevelDestination.label)) },
                    selected = false,
                    onClick = { appState.navigateToTopLevelDestination(topLevelDestination) }
                )
            }
        }
    ) {
        CaptionStudioNavHost(navController = appState.navController)
    }
}

