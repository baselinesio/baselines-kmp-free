package io.baselines.sample.ui.designsystem.components.scrollbar

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlin.math.abs
import kotlin.math.min
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

/**
 * Calculates a [ScrollbarState] driven by the changes in a [LazyListState].
 *
 * @param itemsAvailable the total amount of items available to scroll in the lazy list.
 * @param itemIndex a lookup function for index of an item in the list relative to [itemsAvailable].
 */
@Composable
fun LazyListState.scrollbarState(
    itemsAvailable: Int,
    itemIndex: (LazyListItemInfo) -> Int = LazyListItemInfo::index,
): ScrollbarState =
    scrollbarState(
        itemsAvailable = itemsAvailable,
        visibleItems = { layoutInfo.visibleItemsInfo },
        firstVisibleItemIndex = { visibleItems ->
            interpolateFirstItemIndex(
                visibleItems = visibleItems,
                itemSize = { it.size },
                offset = { it.offset },
                nextItemOnMainAxis = { first -> visibleItems.find { it != first } },
                itemIndex = itemIndex,
            )
        },
        itemPercentVisible = itemPercentVisible@{ itemInfo ->
            itemVisibilityPercentage(
                itemSize = itemInfo.size,
                itemStartOffset = itemInfo.offset,
                viewportStartOffset = layoutInfo.viewportStartOffset,
                viewportEndOffset = layoutInfo.viewportEndOffset,
            )
        },
        reverseLayout = { layoutInfo.reverseLayout },
    )

/**
 * Calculates a [ScrollbarState] driven by the changes in a [LazyGridState]
 *
 * @param itemsAvailable the total amount of items available to scroll in the grid.
 * @param itemIndex a lookup function for index of an item in the grid relative to [itemsAvailable].
 */
@Composable
fun LazyGridState.scrollbarState(
    itemsAvailable: Int,
    itemIndex: (LazyGridItemInfo) -> Int = LazyGridItemInfo::index,
): ScrollbarState =
    scrollbarState(
        itemsAvailable = itemsAvailable,
        visibleItems = { layoutInfo.visibleItemsInfo },
        firstVisibleItemIndex = { visibleItems ->
            interpolateFirstItemIndex(
                visibleItems = visibleItems,
                itemSize = {
                    layoutInfo.orientation.valueOf(it.size)
                },
                offset = { layoutInfo.orientation.valueOf(it.offset) },
                nextItemOnMainAxis = { first ->
                    when (layoutInfo.orientation) {
                        Orientation.Vertical -> visibleItems.find {
                            it != first && it.row != first.row
                        }

                        Orientation.Horizontal -> visibleItems.find {
                            it != first && it.column != first.column
                        }
                    }
                },
                itemIndex = itemIndex,
            )
        },
        itemPercentVisible = itemPercentVisible@{ itemInfo ->
            itemVisibilityPercentage(
                itemSize = layoutInfo.orientation.valueOf(itemInfo.size),
                itemStartOffset = layoutInfo.orientation.valueOf(itemInfo.offset),
                viewportStartOffset = layoutInfo.viewportStartOffset,
                viewportEndOffset = layoutInfo.viewportEndOffset,
            )
        },
        reverseLayout = { layoutInfo.reverseLayout },
    )

/**
 * Calculates a [ScrollbarState] driven by a plain [ScrollState].
 *
 * When the content fits entirely in the viewport (`maxValue == 0`), we fall
 * back to the sentinel `ScrollbarState.FULL`, meaning “no scrollbar needed”.
 */
@Composable
fun ScrollState.scrollbarState(): ScrollbarState {
    var state by remember { mutableStateOf(ScrollbarState.FULL) }
    LaunchedEffect(this) {
        snapshotFlow { value to maxValue }
            .distinctUntilChanged()
            .collect { (offsetPx, maxPx) ->
                state = when {
                    maxPx <= 0 || viewportSize == 0 -> ScrollbarState.FULL
                    else -> {
                        val contentHeight = viewportSize + maxPx
                        ScrollbarState(
                            thumbSizePercent = min(
                                1f,
                                viewportSize.toFloat() / contentHeight
                            ),
                            thumbMovedPercent = offsetPx / maxPx.toFloat()
                        )
                    }
                }
            }
    }
    return state
}

/**
 * Calculates the [ScrollbarState] for lazy layouts.
 * @param itemsAvailable the total amount of items available to scroll in the layout.
 * @param visibleItems a list of items currently visible in the layout.
 * @param firstVisibleItemIndex a function for interpolating the first visible index in the lazy layout
 * as scrolling progresses for smooth and linear scrollbar thumb progression.
 * [itemsAvailable].
 * @param reverseLayout if the items in the backing lazy layout are laid out in reverse order.
 * */
@Composable
internal inline fun <LazyState : ScrollableState, LazyStateItem> LazyState.scrollbarState(
    itemsAvailable: Int,
    crossinline visibleItems: LazyState.() -> List<LazyStateItem>,
    crossinline firstVisibleItemIndex: LazyState.(List<LazyStateItem>) -> Float,
    crossinline itemPercentVisible: LazyState.(LazyStateItem) -> Float,
    crossinline reverseLayout: LazyState.() -> Boolean,
): ScrollbarState {
    var state by remember { mutableStateOf(ScrollbarState.FULL) }

    LaunchedEffect(
        key1 = this,
        key2 = itemsAvailable,
    ) {
        snapshotFlow {
            if (itemsAvailable == 0) return@snapshotFlow null

            val visibleItemsInfo = visibleItems(this@scrollbarState)
            if (visibleItemsInfo.isEmpty()) return@snapshotFlow null

            val firstIndex = min(
                a = firstVisibleItemIndex(visibleItemsInfo),
                b = itemsAvailable.toFloat(),
            )
            if (firstIndex.isNaN()) return@snapshotFlow null

            val itemsVisible = visibleItemsInfo.sumOf {
                itemPercentVisible(it).toDouble()
            }.toFloat()

            val thumbTravelPercent = min(
                a = firstIndex / itemsAvailable,
                b = 1f,
            )
            val thumbSizePercent = min(
                a = itemsVisible / itemsAvailable,
                b = 1f,
            )
            ScrollbarState(
                thumbSizePercent = thumbSizePercent,
                thumbMovedPercent = when {
                    reverseLayout() -> 1f - thumbTravelPercent
                    else -> thumbTravelPercent
                },
            )
        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { state = it }
    }
    return state
}

/**
 * Linearly interpolates the index for the first item in [visibleItems] for smooth scrollbar
 * progression.
 * @param visibleItems a list of items currently visible in the layout.
 * @param itemSize a lookup function for the size of an item in the layout.
 * @param offset a lookup function for the offset of an item relative to the start of the view port.
 * @param nextItemOnMainAxis a lookup function for the next item on the main axis in the direction
 * of the scroll.
 * @param itemIndex a lookup function for index of an item in the layout relative to
 * the total amount of items available.
 *
 * @return a [Float] in the range [firstItemPosition..nextItemPosition) where nextItemPosition
 * is the index of the consecutive item along the major axis.
 * */
private inline fun <LazyState : ScrollableState, LazyStateItem> LazyState.interpolateFirstItemIndex(
    visibleItems: List<LazyStateItem>,
    crossinline itemSize: LazyState.(LazyStateItem) -> Int,
    crossinline offset: LazyState.(LazyStateItem) -> Int,
    crossinline nextItemOnMainAxis: LazyState.(LazyStateItem) -> LazyStateItem?,
    crossinline itemIndex: (LazyStateItem) -> Int,
): Float {
    if (visibleItems.isEmpty()) return 0f

    val firstItem = visibleItems.first()
    val firstItemIndex = itemIndex(firstItem)

    if (firstItemIndex < 0) return Float.NaN

    val firstItemSize = itemSize(firstItem)
    if (firstItemSize == 0) return Float.NaN

    val itemOffset = offset(firstItem).toFloat()
    val offsetPercentage = abs(itemOffset) / firstItemSize

    val nextItem = nextItemOnMainAxis(firstItem) ?: return firstItemIndex + offsetPercentage

    val nextItemIndex = itemIndex(nextItem)

    return firstItemIndex + ((nextItemIndex - firstItemIndex) * offsetPercentage)
}

/**
 * Returns the percentage of an item that is currently visible in the view port.
 * @param itemSize the size of the item
 * @param itemStartOffset the start offset of the item relative to the view port start
 * @param viewportStartOffset the start offset of the view port
 * @param viewportEndOffset the end offset of the view port
 */
private fun itemVisibilityPercentage(
    itemSize: Int,
    itemStartOffset: Int,
    viewportStartOffset: Int,
    viewportEndOffset: Int,
): Float {
    if (itemSize == 0) return 0f
    val itemEnd = itemStartOffset + itemSize
    val startOffset = when {
        itemStartOffset > viewportStartOffset -> 0
        else -> abs(abs(viewportStartOffset) - abs(itemStartOffset))
    }
    val endOffset = when {
        itemEnd < viewportEndOffset -> 0
        else -> abs(abs(itemEnd) - abs(viewportEndOffset))
    }
    val size = itemSize.toFloat()
    return (size - startOffset - endOffset) / size
}
