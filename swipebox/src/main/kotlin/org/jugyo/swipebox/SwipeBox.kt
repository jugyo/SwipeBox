package org.jugyo.swipebox

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntOffset
import kotlin.Unit
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun SwipeBox(
    modifier: Modifier = Modifier,
    state: SwipeBoxState = rememberSwipeBoxState(),
    background: @Composable BoxScope.() -> Unit,
    foreground: @Composable BoxScope.() -> Unit
) {
    Layout(
        modifier = modifier,
        content = {
            Box(content = background)

            Box(
                modifier = Modifier
                    .absoluteOffset {
                        IntOffset(x = min(state.offset, 0f).roundToInt(), y = 0)
                    }
                    .draggable(
                        state = state.draggableState,
                        enabled = state.enabled,
                        orientation = Orientation.Horizontal,
                        onDragStarted = {
                            state.handleDragStarted()
                        },
                        onDragStopped = {
                            state.handleDragStopped()
                        }
                    )
                    .background(Color.White),
                content = foreground
            )
        }
    ) { measurables, constraints ->
        val _foreground = measurables[1].measure(
            constraints.copy(
                minWidth = 0,
                minHeight = 0
            )
        )
        val _background = measurables[0].measure(
            constraints.copy(
                minWidth = 0,
                minHeight = 0,
                maxHeight = _foreground.height
            )
        )
        state.backgroundWidthPx = _background.width

        layout(width = _foreground.width, height = _foreground.height) {
            _background.place(_foreground.width - _background.width, 0)
            _foreground.place(0, 0)
        }
    }
}
