package io.baselines.sample.ui.designsystem.components.scrollbar

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.baselines.sample.ui.designsystem.theme.AppTheme

@Composable
fun LazyGridScrollbar(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    reverseLayout: Boolean = false,
    state: LazyGridState = rememberLazyGridState(),
    content: @Composable BoxScope.(scrollState: LazyGridState) -> Unit,
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
