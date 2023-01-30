package org.jugyo.swipebox

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.*
import kotlin.math.abs

@Stable
class SwipeBoxState internal constructor(
    val animationDurationMillis: Int,
    val swipeThresholdRatio: Float
) {

    var enabled by mutableStateOf(true)

    var isOpen by mutableStateOf(false)
        private set

    var isDragging by mutableStateOf(false)
        private set

    val progress by derivedStateOf { abs(offset) / backgroundWidthPx }

    var offset: Float by mutableStateOf(0f)
        private set

    internal var backgroundWidthPx by mutableStateOf(0)

    internal val draggableState = DraggableState { delta ->
        if (offset + delta < 0 && abs(offset + delta) < backgroundWidthPx) {
            offset += delta
        }
    }

    private val swipeThresholdPx get() = backgroundWidthPx * swipeThresholdRatio

    private val offsetAnimatable = Animatable(0f)

    suspend fun open() {
        animateTo(-backgroundWidthPx.toFloat())
        isOpen = true
    }

    suspend fun close() {
        animateTo(0f)
        isOpen = false
    }

    private suspend fun animateTo(targetValue: Float) {
        if (offset == targetValue) {
            return
        }

        offsetAnimatable.snapTo(offset)
        offsetAnimatable.animateTo(
            targetValue,
            tween(durationMillis = animationDurationMillis)
        ) {
            offset = value
        }
    }

    suspend fun handleDragStopped() {
        try {
            if (isOpen) {
                if (abs(backgroundWidthPx) - abs(offset) > swipeThresholdPx) {
                    close()
                } else {
                    open()
                }
            } else {
                if (abs(offset) < swipeThresholdPx) {
                    close()
                } else {
                    open()
                }
            }
        } finally {
            isDragging = false
        }
    }

    suspend fun handleDragStarted() {
        offsetAnimatable.stop()
        isDragging = true
    }
}

private val DefaultAnimationDurationMillis = 200
private val SwipeThresholdRatio = 0.25f

@Composable
fun rememberSwipeBoxState(
    key: Any? = null,
    animationDurationMillis: Int = DefaultAnimationDurationMillis,
    swipeThresholdRatio: Float = SwipeThresholdRatio
) = remember(key) {
    SwipeBoxState(
        animationDurationMillis = animationDurationMillis,
        swipeThresholdRatio = swipeThresholdRatio
    )
}
