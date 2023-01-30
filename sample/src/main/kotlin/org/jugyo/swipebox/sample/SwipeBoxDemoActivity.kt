package org.jugyo.swipebox.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jugyo.swipebox.SwipeBox
import org.jugyo.swipebox.rememberSwipeBoxState

class SwipeBoxDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Screen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen() {
    val scope = rememberCoroutineScope()
    val swipeBoxState = rememberSwipeBoxState()

    val backgroundContentColor: Color by animateColorAsState(
        when {
            swipeBoxState.progress > 0.25 -> Color.Cyan
            else -> Color.Gray
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            SwipeBox(
                state = swipeBoxState,
                foreground = {
                    ForegroundContent()
                },
                background = {
                    BackgroundContent(modifier = Modifier.background(backgroundContentColor))
                }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "swipeBoxState.enabled: ${swipeBoxState.enabled}")
                Text(text = "swipeBoxState.isDragging: ${swipeBoxState.isDragging}")
                Text(text = "swipeBoxState.isOpen: ${swipeBoxState.isOpen}")
                Text(text = "swipeBoxState.progress: ${swipeBoxState.progress}")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    enabled = !swipeBoxState.isOpen,
                    onClick = {
                        scope.launch {
                            swipeBoxState.open()
                        }
                    }) {
                    Text("Open")
                }
                Button(
                    enabled = swipeBoxState.isOpen,
                    onClick = {
                        scope.launch {
                            swipeBoxState.close()
                        }
                    }) {
                    Text("Close")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = swipeBoxState.enabled, onCheckedChange = {
                        swipeBoxState.enabled = it
                    })
                    Text("Drag Enabled")
                }
            }
        }
    }
}

@Composable
private fun ForegroundContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(240.dp)
            .background(Color.LightGray)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "← Swipe"
        )
    }
}

@Composable
private fun BackgroundContent(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight()
            .then(modifier)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "\uD83D\uDE42"
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Screen()
}
