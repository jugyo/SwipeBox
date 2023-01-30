package org.jugyo.swipebox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Stable
class SwipeListState<T> internal constructor(
    val animationDurationMillis: Int,
    val swipeThresholdRatio: Float
) {
    private var stateMap = mutableMapOf<T, SwipeBoxState>()

    @Composable
    fun rememberSwipeBoxState(key: T): SwipeBoxState {
        val swipeBoxState = remember(key) {
            SwipeBoxState(
                animationDurationMillis,
                swipeThresholdRatio
            )
        }

        stateMap[key] = swipeBoxState

        LaunchedEffect(swipeBoxState.isDragging) {
            if (swipeBoxState.isDragging) {
                disableOthers(swipeBoxState)
            } else {
                enableAll()
            }
        }

        return swipeBoxState
    }

    suspend fun open(key: T) {
        stateMap[key]?.let {
            disableOthers(it)
            it.open()
            enableAll()
        }
    }

    suspend fun closeAll() {
        stateMap.values.filter { it.isOpen }.forEach { state ->
            state.close()
        }
    }

    private suspend fun disableOthers(active: SwipeBoxState) {
        val others = stateMap.values.filter { it != active }
        others.filter { it.enabled }.forEach { state ->
            state.enabled = false
        }
        others.filter { it.isOpen }.forEach { state ->
            state.close()
        }
    }

    private fun enableAll() {
        stateMap.values.filter { !it.enabled }.forEach { state ->
            state.enabled = true
        }
    }
}

private val DefaultAnimationDurationMillis = 200
private val SwipeThresholdRatio = 0.25f

@Composable
fun <T> rememberSwipeListState(
    animationDurationMillis: Int = DefaultAnimationDurationMillis,
    swipeThresholdRatio: Float = SwipeThresholdRatio

): SwipeListState<T> {
    val state = remember {
        SwipeListState<T>(
            animationDurationMillis = animationDurationMillis,
            swipeThresholdRatio = swipeThresholdRatio
        )
    }

    return state
}
