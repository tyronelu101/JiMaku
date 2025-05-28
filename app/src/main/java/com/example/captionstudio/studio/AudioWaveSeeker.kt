package com.example.captionstudio.studio

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.captionstudio.app.toPx
import kotlin.math.abs
import kotlin.math.roundToInt

private val MAX_WAVE_BAR_HEIGHT = 80.dp
private val MIN_WAVE_BAR_HEIGHT = 8.dp
private val BAR_WIDTH = 3.dp
private val GAP_BETWEEN_BARS = BAR_WIDTH + 4.dp

@Composable
fun AudioWaveSeeker(allowSeek: Boolean, amplitudes: List<Float>, modifier: Modifier = Modifier) {

    val configuration = LocalConfiguration.current
    val screenWidthDp: Dp = configuration.screenWidthDp.dp
    val center = screenWidthDp / 2

    var offset by remember { mutableFloatStateOf(0f) }

    var scrollTick = 0f
    val scrollThreshold = GAP_BETWEEN_BARS.toPx()
    val width = amplitudes.size * GAP_BETWEEN_BARS.toPx()
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (allowSeek) modifier.scrollable(
                    orientation = Orientation.Horizontal,
                    state = rememberScrollableState { delta ->
                        scrollTick += delta
                        Log.i("Test", "offset is ${offset} width is ${width}")

                        if ((offset in 0f..width) && abs(scrollTick) >= scrollThreshold) {
                            offset = if (scrollTick < 0) {
                                maxOf(offset - scrollThreshold, 0f)
                            } else {
                                minOf(offset + scrollThreshold, width)
                            }
                            scrollTick = 0f
                        }
                        Log.i("Test", "Offset is ${offset}")
                        delta
                    }) else modifier
            )
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
        ) {
            //Left side drawing wave bars
            Box(
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.5f)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    translate(
                        if (allowSeek) offset
                        else {
                            offset = 0f
                            0f
                        }
                    ) {
                        //Handle auto scrolling
                        val translate =
                            center.toPx() - (GAP_BETWEEN_BARS.toPx() * amplitudes.size) - BAR_WIDTH.toPx() / 2f
                        withTransform({ translate(translate) }) {
                            var offSetX = 0f
                            for (amplitude in amplitudes) {
                                val waveHeight =
                                    if (amplitude < 0.1f) MIN_WAVE_BAR_HEIGHT else MAX_WAVE_BAR_HEIGHT * amplitude
                                val offsetY = (size.height - waveHeight.toPx()) / 2
                                drawRoundRect(
                                    Color.White,
                                    topLeft = Offset(offSetX, offsetY),
                                    size = Size(BAR_WIDTH.toPx(), waveHeight.toPx()),
                                    cornerRadius = CornerRadius(x = 48f, y = 48f)
                                )
                                offSetX += GAP_BETWEEN_BARS.toPx()
                            }
                        }

                        //Right side empty static wave bars
                        var currentOffsetX = center.toPx() - (BAR_WIDTH.toPx() / 2f)

                        //draw empty wave form bars on right side of vertical line
                        val availableSpace =
                            ((screenWidthDp.toPx() - GAP_BETWEEN_BARS.toPx()) / 2f) / (GAP_BETWEEN_BARS.toPx() + BAR_WIDTH.toPx())
                        val count = (screenWidthDp.toPx() / 2f) / (availableSpace)
                        val offsetY = (size.height - MIN_WAVE_BAR_HEIGHT.toPx()) / 2
                        for (i in 0..count.roundToInt()) {
                            drawRoundRect(
                                Color.LightGray,
                                topLeft = Offset(currentOffsetX, offsetY),
                                size = Size(BAR_WIDTH.toPx(), MIN_WAVE_BAR_HEIGHT.toPx()),
                                cornerRadius = CornerRadius(x = 48f, y = 48f)
                            )
                            currentOffsetX += GAP_BETWEEN_BARS.toPx()
                        }
                    }
                }

            }
        }

        //Draw vertical line
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = Color.White,
                strokeWidth = 1.dp.toPx(),
                start = Offset(x = center.toPx(), y = 0f),
                end = Offset(x = center.toPx(), size.height)
            )
        }
    }
}