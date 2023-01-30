package org.jugyo.swipebox.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jugyo.swipebox.SwipeBox
import org.jugyo.swipebox.SwipeBoxState
import org.jugyo.swipebox.rememberSwipeListState

class SwipeBoxWithLazyListDemoActivity : AppCompatActivity() {
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
    val lazyListState = rememberLazyListState()
    val swipeListState = rememberSwipeListState<Int>()

    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (lazyListState.isScrollInProgress) {
            swipeListState.closeAll()
        }
    }

    Column(
        modifier = Modifier.background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(vertical = 16.dp)
        ) {
            StatusText(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = "isScrollInProgress: ${lazyListState.isScrollInProgress}"
            )
        }

        LazyColumn(
            state = lazyListState
        ) {
            items(12) { index ->
                val swipeBoxState = swipeListState.rememberSwipeBoxState(index)

                Column {
                    SwipeBox(
                        state = swipeBoxState,
                        foreground = {
                            ListItem(
                                enabled = !swipeBoxState.isDragging && !swipeBoxState.isOpen,
                                swipeBoxState = swipeBoxState
                            )
                        },
                        background = {
                            ListItemActions(
                                enabled = !swipeBoxState.isDragging && swipeBoxState.isOpen
                            )
                        }
                    )
                    Divider(color = Color(0xFFF4F4F4))
                }
            }
        }
    }
}

@Composable
private fun ListItem(enabled: Boolean, swipeBoxState: SwipeBoxState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(enabled = enabled, onClick = {})
            .padding(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.1f))
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(20.dp)
                        .background(Color.Black.copy(alpha = 0.1f))
                )
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(12.dp)
                        .background(Color.Black.copy(alpha = 0.1f))
                )
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(12.dp)
                        .background(Color.Black.copy(alpha = 0.1f))
                )
            }
        }
        StatusText(
            modifier = Modifier.align(Alignment.BottomEnd),
            text = "state(enabled: ${swipeBoxState.enabled}, " +
                "isOpen: ${swipeBoxState.isOpen}, " +
                "isDragging: ${swipeBoxState.isDragging})",
        )
    }
}

@Composable
private fun StatusText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(fontSize = 11.sp, color = Color.LightGray)
    )
}

@Composable
private fun ListItemActions(modifier: Modifier = Modifier, enabled: Boolean) {
    val alphaAnimation by animateFloatAsState(
        targetValue = when {
            enabled -> 1f
            else -> 0.6f
        },
        tween(durationMillis = 400)
    )

    Row(
        modifier = Modifier
            .background(Color(0xFFEEEEEE))
            .alpha(alphaAnimation)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        SwipeBoxActionButton(
            icon = Icons.Rounded.Share,
            label = "Share",
            enabled = enabled,
            onClick = {}
        )
        SwipeBoxActionButton(
            icon = Icons.Rounded.Favorite,
            label = "Favorite",
            enabled = enabled,
            onClick = {}
        )
        SwipeBoxActionButton(
            icon = Icons.Rounded.Delete,
            label = "Delete",
            enabled = enabled,
            onClick = {}
        )
    }
}

@Composable
private fun SwipeBoxActionButton(
    icon: ImageVector,
    label: String,
    width: Dp = 80.dp,
    color: Color = Color(0xFFF4F4F4),
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(width)
            .background(color)
            .clickable(enabled = enabled, onClick = onClick, role = Role.Button)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color.Gray)
            Text(label, style = TextStyle(color = Color.Gray))
        }
    }
}

@Preview
@Composable
private fun Preview1() {
    MaterialTheme {
        Screen()
    }
}
