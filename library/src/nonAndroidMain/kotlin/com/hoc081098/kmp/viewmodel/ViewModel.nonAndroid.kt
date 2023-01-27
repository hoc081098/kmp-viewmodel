package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.internal.AtomicBoolean
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@Suppress("NOTHING_TO_INLINE")
private inline fun viewModelScopeDispatcher(): CoroutineDispatcher =
  runCatching { Dispatchers.Main.immediate }
    .getOrDefault(Dispatchers.Main)

public actual abstract class ViewModel public actual constructor() {
  private val isCleared = AtomicBoolean(false)
  private val coroutineScopeLazy = lazy {
    CoroutineScope(SupervisorJob() + viewModelScopeDispatcher())
  }

  protected actual val viewModelScope: CoroutineScope by coroutineScopeLazy
  protected actual open fun onCleared(): Unit = Unit

  /**
   * When using it on non Android platforms (eg. `iOS`, `macOS`, ...) you'll want to make sure that
   * you call [clear] on your ViewModel on `deinit` to properly cancel the CoroutineScope.
   *
   * This method is thread-safe, ie. it can be called from any thread.
   */
  @Suppress("unused") // Called by platform code
  public fun clear() {
    if (isCleared.compareAndSet(expectedValue = false, newValue = true)) {
      if (coroutineScopeLazy.isInitialized()) {
        coroutineScopeLazy.value.cancel()
      }
      onCleared()
    }
  }
}
