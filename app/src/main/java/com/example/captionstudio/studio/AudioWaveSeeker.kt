package com.example.captionstudio.studio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val MAX_WAVE_BAR_HEIGHT = 80.dp
private val MIN_WAVE_BAR_HEIGHT = 8.dp
private val BAR_WIDTH = 3.dp
private val GAP_BETWEEN_BARS = BAR_WIDTH + 4.dp

@Composable
fun AudioWaveSeeker(amplitudes: List<Float>, modifier: Modifier = Modifier) {

    val scrollState = rememberScrollState()

    val configuration = LocalConfiguration.current
    val screenWidthDp: Dp = configuration.screenWidthDp.dp
    val center = screenWidthDp / 2

    val currentCanvasWidth = (3.dp + 3.dp) * amplitudes.size

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(modifier = modifier.fillMaxSize()) {
            //Left side drawing wave bars
            Box(
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.5f)
                    .horizontalScroll(scrollState)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(if (currentCanvasWidth < screenWidthDp) screenWidthDp / 2 else currentCanvasWidth)
                ) {
                        amplitudes.forEachIndexed { index, amplitude ->
                            val waveHeight =
                                if (amplitude < 0.1f) MIN_WAVE_BAR_HEIGHT else MAX_WAVE_BAR_HEIGHT * amplitude
                            val offsetY = (size.height - waveHeight.toPx()) / 2
                            val offSetX =
                                center.toPx() - (amplitudes.size - (index)) * (GAP_BETWEEN_BARS).toPx()
                            drawRoundRect(
                                Color.White,
                                topLeft = Offset(offSetX, offsetY),
                                size = Size(BAR_WIDTH.toPx(), waveHeight.toPx()),
                                cornerRadius = CornerRadius(x = 48f, y = 48f)
                            )
                        }

                }
            }
            //Right side empty static wave bars
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.5f)
            ) {
                var currentOffsetX = GAP_BETWEEN_BARS.toPx()

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