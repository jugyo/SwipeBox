package org.jugyo.swipebox.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
private fun Screen() {
    val scope = rememberCoroutineScope()
    val swipeBoxState = rememberSwipeBoxState(
        animationDurationMillis = 400
    )

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
                Text(text = "swipeBoxState.offset: ${swipeBoxState.offset}")
                Text(text = "swipeBoxState.progress: ${swipeBoxState.progress}")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            swipeBoxState.open()
                        }
                    }) {
                    Text("Open")
                }
                Button(
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
    MaterialTheme {
        Screen()
    }
}
