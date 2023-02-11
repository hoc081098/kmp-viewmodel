package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.internal.AtomicBoolean
import com.hoc081098.kmp.viewmodel.internal.synchronized
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@Suppress("NOTHING_TO_INLINE")
private inline fun viewModelScopeDispatcher(): CoroutineDispatcher =
  runCatching { Dispatchers.Main.immediate }
    .getOrDefault(Dispatchers.Main)

public actual abstract class ViewModel : Lockable {
  private val isCleared = AtomicBoolean(false)
  private val closeables: MutableSet<Closeable>

  private val coroutineScopeLazy = lazy {
    check(!isCleared.value) { "Cannot access viewModelScope on a cleared ViewModel" }
    CoroutineScope(SupervisorJob() + viewModelScopeDispatcher())
  }
  protected actual val viewModelScope: CoroutineScope by coroutineScopeLazy

  public actual constructor() : super() {
    closeables = linkedSetOf()
  }

  public actual constructor(vararg closeables: Closeable) : super() {
    this.closeables = linkedSetOf<Closeable>().apply { addAll(closeables) }
  }

  protected actual open fun onCleared(): Unit = Unit

  public actual fun addCloseable(closeable: Closeable) {
    synchronized(this) {
      check(!isCleared.value) { "Cannot access viewModelScope on a cleared ViewModel" }
      closeables += closeable
    }
  }

  /**
   * When using it on non Android platforms (eg. `iOS`, `macOS`, ...) you'll want to make sure that
   * you call [clear] on your ViewModel on `deinit` to properly cancel the CoroutineScope.
   *
   * This method is thread-safe, ie. it can be called from any thread.
   */
  @Suppress("unused") // Called by platform code
  public fun clear(): Unit = synchronized(this) {
    if (isCleared.compareAndSet(expectedValue = false, newValue = true)) {
      if (coroutineScopeLazy.isInitialized()) {
        coroutineScopeLazy.value.cancel()
      }

      closeables.forEach { it.close() }
      closeables.clear()

      onCleared()
    }
  }
}
