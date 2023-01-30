# SwipeBox

Swipe-to-reveal UI component for Jetpack Compose.

## Basic usage

`SwipeBox` composable takes 3 required parameters such as `state`, `foreground`, and `background` (and `modifier` as needed).

| Parameter  | Type | Description |
| ------------- | ------------- | ------------- |
| state  | `SwipeBoxState` | It's for managing the state of the `SwipeBox` |
| foreground  | `@Composable BoxScope.() -> Unit`  | A composable that's shown in foreground |
| background  | `@Composable BoxScope.() -> Unit`  | A composable for background, it reveals when foreground is swiped |

```kotlin
@Composable
fun ForegroundContent() {
    Box(
        modifier = Modifier.size(240.dp).background(Color.LightGray)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "← Swipe"
        )
    }
}

@Composable
fun BackgroundContent() {
    Box(
        modifier = Modifier.width(200.dp).fillMaxHeight().background(Color.Cyan)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "\uD83D\uDE42"
        )
    }
}

@Preview
@Composable
fun Preview() {
    val swipeBoxState = rememberSwipeBoxState()

    SwipeBox(
        state = swipeBoxState,
        foreground = {
            ForegroundContent()
        },
        background = {
            BackgroundContent()
        }
    )
}
```

![SwipeBoxSample.gif](https://github.com/jugyo/SwipeBox/blob/main/screenshots/SwipeBoxSample.gif?raw=true)

## Usage of GroupedSwipeBoxState

To integrate `SwipeBox` to `LazyColumn`, this library provides `GroupedSwipeBoxState` so you can manager all the states of child `SwipeBox`s in one hand.

```kotlin
@Preview
@Composable
private fun Preview() {
    val lazyListState = rememberLazyListState()
    val groupedSwipeBoxState = rememberGroupedSwipeBoxState<Int>()

    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (lazyListState.isScrollInProgress) {
            groupedSwipeBoxState.closeAll()
        }
    }

    LazyColumn(
        state = lazyListState
    ) {
        items(12) { index ->
            val swipeBoxState = groupedSwipeBoxState.stateFor(index)

            Column {
                SwipeBox(
                    state = swipeBoxState,
                    foreground = {
                        ListItem(
                            enabled = !swipeBoxState.isDragging && !swipeBoxState.isOpen,
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
```

![GroupedSwipeBoxSample.gif](https://github.com/jugyo/SwipeBox/blob/main/screenshots/GroupedSwipeBoxSample.gif?raw=true)
