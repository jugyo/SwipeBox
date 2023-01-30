package org.jugyo.swipebox

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.abs

@Stable
class SwipeBoxState internal constructor(
    val animationDurationMillis: Int,
    val swipeThresholdRatio: Float
) {
    private val mutex = Mutex()

    internal var offset: Float by mutableStateOf(0f)
        private set

    internal var backgroundWidthPx by mutableStateOf(0)

    internal val draggableState = DraggableState { delta ->
        if (offset + delta < 0 && abs(offset + delta) < backgroundWidthPx) {
            offset += delta
        }
    }

    var enabled by mutableStateOf(true)

    var isOpen by mutableStateOf(false)
        private set

    var isDragging by mutableStateOf(false)
        private set

    val progress by derivedStateOf { abs(offset) / backgroundWidthPx }

    private val swipeThresholdPx get() = backgroundWidthPx * swipeThresholdRatio

    suspend fun open() {
        dragTo(-backgroundWidthPx.toFloat())
        isOpen = true
    }

    suspend fun close() {
        dragTo(0f)
        isOpen = false
    }

    private suspend fun dragTo(targetValue: Float) {
        if (offset == targetValue) {
            return
        }

        mutex.withLock {
            draggableState.drag(MutatePriority.PreventUserInput) {
                // TODO: Use Animatable
                Animatable(offset).animateTo(
                    targetValue = targetValue,
                    tween(durationMillis = animationDurationMillis)
                ) {
                    offset = value
                }
            }
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

    fun handleDragStarted() {
        isDragging = true
    }
}

private val DefaultAnimationDurationMillis = 200
private val SwipeThresholdRatio = 0.25f

@Composable
fun rememberSwipeBoxState(
    animationDurationMillis: Int = DefaultAnimationDurationMillis,
    swipeThresholdRatio: Float = SwipeThresholdRatio
) = remember {
    SwipeBoxState(
        animationDurationMillis = animationDurationMillis,
        swipeThresholdRatio = swipeThresholdRatio
    )
}
