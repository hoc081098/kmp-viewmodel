package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner

/**
 * The CompositionLocal containing the current [ViewModelStoreOwner] or `null` if not provided.
 */
public val LocalViewModelStoreOwner: ProvidableCompositionLocal<ViewModelStoreOwner?> =
  staticCompositionLocalOf { null }

/**
 * Provides [ViewModelStoreOwner] as [LocalViewModelStoreOwner] to the [content].
 */
@Composable
public fun ViewModelStoreOwnerProvider(
  viewModelStoreOwner: ViewModelStoreOwner,
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(
    LocalViewModelStoreOwner provides viewModelStoreOwner,
    content = content,
  )
}
