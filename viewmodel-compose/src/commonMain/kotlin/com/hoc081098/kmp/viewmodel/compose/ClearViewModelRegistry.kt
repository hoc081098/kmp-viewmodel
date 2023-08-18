package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.ViewModel

/**
 * A class that responsible for clearing [ViewModel]s when needed.
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
