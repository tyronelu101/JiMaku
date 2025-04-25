package com.example.captionstudio.app.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import com.example.captionstudio.R
import com.example.captionstudio.app.ui.CaptionStudioIcons
import com.example.captionstudio.audiosource.AudioRoute
import com.example.captionstudio.transcriptions.TranscriptionsRoute
import kotlin.reflect.KClass

enum class TopLevelDestinations(
    val icon: ImageVector,
    @StringRes val label: Int,
    @StringRes val contentDescription: Int,
    val route: KClass<*>
) {
    AUDIO_SOURCE(
        icon = CaptionStudioIcons.AUDIO_SOURCE,
        label = R.string.audio_source,
        contentDescription = R.string.audio_source,
        route = AudioRoute::class,
    ),
    TRANSCRIPTIONS(
        icon = CaptionStudioIcons.TRANSCRIPTIONS,
        label = R.string.transcriptions,
        contentDescription = R.string.transcriptions,
        route = TranscriptionsRoute::class,
    ),
}

private fun navigateToTopLevelDestination(topLevelDestinations: TopLevelDestinations) {
    val topLevelNavOptions = navOptions {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
    when(topLevelDestinations) {
        TopLevelDestinations.AUDIO_SOURCE ->
    }

}