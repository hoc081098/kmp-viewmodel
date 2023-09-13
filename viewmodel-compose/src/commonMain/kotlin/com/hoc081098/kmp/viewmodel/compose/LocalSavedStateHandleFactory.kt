package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory

/**
 * The CompositionLocal containing the current [SavedStateHandleFactory] or `null` if not provided.
 */
@Suppress("CompositionLocalAllowlist")
public val LocalSavedStateHandleFactory: ProvidableCompositionLocal<SavedStateHandleFactory?> =
  staticCompositionLocalOf { null }

/**
 * Provides [SavedStateHandleFactory] as [LocalSavedStateHandleFactory] to the [content].
 */
@Composable
public fun SavedStateHandleFactoryProvider(
  savedStateHandleFactory: SavedStateHandleFactory,
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(
    LocalSavedStateHandleFactory provides savedStateHandleFactory,
    content = content,
  )
}
