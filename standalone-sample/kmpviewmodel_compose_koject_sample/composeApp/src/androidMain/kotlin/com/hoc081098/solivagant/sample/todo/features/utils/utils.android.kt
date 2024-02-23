@file:JvmName("Utils")

package com.hoc081098.solivagant.sample.todo.features.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Credits:
// https://github.com/JetBrains/compose-multiplatform/blob/4eb808620a239bd34170864f7de5c70c9e6464d3/examples/todoapp-lite/shared/src/androidMain/kotlin/example/todoapp/lite/common/Utils.kt

internal actual val MARGIN_SCROLLBAR: Dp = 0.dp

internal actual interface ScrollbarAdapter

@Composable
internal actual fun rememberScrollbarAdapter(scrollState: LazyListState): ScrollbarAdapter =
  object : ScrollbarAdapter {}

@Composable
internal actual fun VerticalScrollbar(
  modifier: Modifier,
  adapter: ScrollbarAdapter,
) {
  // no-op
}
