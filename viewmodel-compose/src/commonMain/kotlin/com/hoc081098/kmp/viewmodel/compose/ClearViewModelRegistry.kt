package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.ViewModel

/**
 * A class that responsible for clearing [ViewModel]s when needed.
 *
 * ## On Android
 *
 * This class is not used because [ViewModel]s are cleared automatically by AndroidX Lifecycle.
 */
public class ClearViewModelRegistry {
  private var isCleared = false
  private val callbacks = mutableMapOf<Any, () -> Unit>()

  @MainThread
  internal fun register(key: Any, factory: () -> () -> Unit) {
    if (isCleared) {
      factory()()
      return
    }

    callbacks.getOrPut(key, factory)
  }

  /**
   * Clears registered [ViewModel]s.
   */
  @MainThread
  public fun clear() {
    isCleared = true

    callbacks.values.forEach { it() }
    callbacks.clear()
  }
}

@Composable
public fun rememberClearViewModelRegistry(): ClearViewModelRegistry =
  remember { ClearViewModelRegistry() }

/**
 * The CompositionLocal containing the current [ClearViewModelRegistry] or `null` if not provided.
 */
public val LocalClearViewModelRegistry: ProvidableCompositionLocal<ClearViewModelRegistry?> =
  staticCompositionLocalOf { null }

/**
 * Returns current composition local value for the [ClearViewModelRegistry]
 * or `null` if one has not been provided.
 */
@Composable
public inline fun currentClearViewModelRegistry(): ClearViewModelRegistry? =
  LocalClearViewModelRegistry.current

/**
 * Provides [ClearViewModelRegistry] as [LocalClearViewModelRegistry] to the [content].
 */
@Composable
public fun ClearViewModelRegistryProvider(
  clearViewModelRegistry: ClearViewModelRegistry?,
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(
    LocalClearViewModelRegistry provides clearViewModelRegistry,
    content = content,
  )
}
