# SwipeBox

Swipe-to-reveal UI component for Jetpack Compose.



## Usage

`SwipeBox` composable takes 3 required parameters such as `state`, `foreground`, and `background` (and `modifier` as needed).

| Parameters  | Type | Description |
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
