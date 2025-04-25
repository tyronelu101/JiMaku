package com.example.captionstudio.app.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.captionstudio.R
import com.example.captionstudio.app.ui.CaptionStudioIcons
import com.example.captionstudio.audiosource.StudioModeRoute
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
        route = StudioModeRoute::class,
    ),
    TRANSCRIPTIONS(
        icon = CaptionStudioIcons.TRANSCRIPTIONS,
        label = R.string.transcriptions,
        contentDescription = R.string.transcriptions,
        route = TranscriptionsRoute::class,
    ),
}

