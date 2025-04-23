package com.example.jimaku

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    HOME(R.string.home, Icons.Default.Home, R.string.home),
    FILES(R.string.files, Icons.AutoMirrored.Default.List, R.string.files)
}

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = stringResource(id = it.contentDescription)
                        )
                    },
                    label = { Text(text = stringResource(id = it.label)) },
                    selected = true,
                    onClick = { navController.navigate(it) }
                )
            }
        }
    ) {
        JiMakuNavHost(navController = navController)
    }
}