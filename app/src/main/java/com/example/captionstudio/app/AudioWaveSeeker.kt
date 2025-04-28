package com.example.captionstudio.app

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalDragOrCancellation
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AudioWaveFormSeeker(amplitudes: List<Float>, modifier: Modifier = Modifier) {
    val waveWidth = 8.dp.toPx()
    val gap = waveWidth + 2.dp.toPx()
    var verticalLinePosition by remember {
        mutableFloatStateOf(-1F)
    }
    val configuration = LocalConfiguration.current
    val screenWidthDp: Dp = configuration.screenWidthDp.dp
    val scrollState = rememberScrollState()
    Box(
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(scrollState)
    ) {
        Canvas(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxHeight()
                .width(screenWidthDp)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        val downPointerId = down.id
                        verticalLinePosition = down.position.x

                        val touchSlopChange =
                            awaitHorizontalTouchSlopOrCancellation(down.id) { change, _ ->
                                Log.i("Test", "Touch slop reached.")
                                change.consume()
                            }
                        Log.i("Test", "Touch slop is $touchSlopChange")

                        if (touchSlopChange != null) {
                            while (touchSlopChange.pressed) {
                                val drag = awaitHorizontalDragOrCancellation(downPointerId) ?: break
                                val deltaX = drag.positionChange().x
                                drag.consume()
                            }
                            Log.i("Test", "Pointer up")
                        } else {
                            Log.i("Test", "Touch slop cancelled")
                        }
                    }
                }

        ) {
            //Draw horizontal line
            drawLine(
                color = Color.Red,
                strokeWidth = 1.dp.toPx(),
                start = Offset(x = 0f, y = size.height / 2),
                end = Offset(size.width, size.height / 2)
            )
            //Draw vertical line
            drawLine(
                color = Color.Red,
                strokeWidth = 1.dp.toPx(),
                start = Offset(x = size.width / 2, y = 0f),
                end = Offset(x = size.width / 2, size.height)
            )

            var currentOffsetX = size.width / 2
            amplitudes.forEach { amplitude ->
                val waveHeight = (amplitude * size.height) * 0.9f
                val offsetY = (size.height - waveHeight) / 2

                drawRoundRect(
                    Color.White,
                    topLeft = Offset(currentOffsetX, offsetY),
                    size = Size(waveWidth, waveHeight),
                    cornerRadius = CornerRadius(x = 48f, y = 48f)
                )
                currentOffsetX += gap
            }
//            Max amplitude: 32767
            if (verticalLinePosition != -1F) {
                drawLine(
                    color = Color.Blue,
                    strokeWidth = 3.dp.toPx(),
                    start = Offset(x = verticalLinePosition, y = 0.dp.toPx()),
                    end = Offset(x = verticalLinePosition, y = size.height),
                )
            }
        }
    }
}