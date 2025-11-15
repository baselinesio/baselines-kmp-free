package io.baselines.sample.ui.designsystem.components.scrollbar

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.baselines.sample.ui.designsystem.theme.AppTheme

@Composable
fun LazyColumnScrollbar(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    reverseLayout: Boolean = false,
    state: LazyListState = rememberLazyListState(),
    content: @Composable BoxScope.(scrollState: LazyListState) -> Unit,
) {
    val itemsCount by remember {
        derivedStateOf { state.layoutInfo.totalItemsCount }
    }
    val scrollbarState = state.scrollbarState(itemsCount)
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
