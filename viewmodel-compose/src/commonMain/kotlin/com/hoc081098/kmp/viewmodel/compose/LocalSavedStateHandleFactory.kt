package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.createSavedStateHandle

/**
 * The CompositionLocal containing the current [SavedStateHandleFactory] or `null` if not provided.
 *
 * @see [createSavedStateHandle]
 */
@Suppress("CompositionLocalAllowlist")
public val LocalSavedStateHandleFactory: ProvidableCompositionLocal<SavedStateHandleFactory?> =
  staticCompositionLocalOf { null }

/**
 * Provides [SavedStateHandleFactory] as [LocalSavedStateHandleFactory] to the [content].
 *
 * @see [createSavedStateHandle]
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
