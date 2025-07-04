package io.baselines.sample.ui.designsystem.components.scrollbar

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.baselines.sample.ui.designsystem.theme.AppTheme

@Composable
fun ColumnScrollbar(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    reverseLayout: Boolean = false,
    state: ScrollState = rememberScrollState(),
    content: @Composable BoxScope.(scrollState: ScrollState) -> Unit,
) {
    val scrollbarState = state.scrollbarState()
    Box(modifier = modifier) {
        content(state)
        if (visible) {
            state.DecorativeScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = AppTheme.spacings.elementSmall)
                    .align(Alignment.CenterEnd),
                state = scrollbarState,
                orientation = Orientation.Vertical,
                reverseLayout = reverseLayout,
            )
        }
    }
}
