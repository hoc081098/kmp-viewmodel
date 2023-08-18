package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.MainThread

public fun interface Runnable {
  public fun run()
}

public class ClearViewModelRegistry {
  private var isCleared = false
  private val callbacks = mutableMapOf<Any, Runnable>()

  @MainThread
  internal fun register(key: Any, factory: () -> Runnable) {
    if (isCleared) {
      factory().run()
      return
    }

    callbacks.getOrPut(key, factory)
  }

  @MainThread
  public fun clear() {
    isCleared = true

    callbacks.values.forEach { it.run() }
    callbacks.clear()
  }
}

@Composable
public fun rememberClearViewModelRegistry(): ClearViewModelRegistry =
  remember { ClearViewModelRegistry() }
