package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.internal.AtomicBoolean
import com.hoc081098.kmp.viewmodel.internal.synchronized
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@Suppress("NOTHING_TO_INLINE")
private inline fun CoroutineDispatcher.test() = apply {
  // Will throw an exception if this is MissingMainCoroutineDispatcher.
  isDispatchNeeded(EmptyCoroutineContext)
}

private fun viewModelScopeDispatcher(): CoroutineDispatcher =
  runCatching { Dispatchers.Main.immediate.test() }
    .recoverCatching { Dispatchers.Main.test() }
    .getOrDefault(Dispatchers.Default)

public actual abstract class ViewModel : Any {
  private val lockable = Lockable()
  private val isCleared = AtomicBoolean(false)
  private val closeables: MutableSet<Closeable>

  private val coroutineScopeLazy = lazy {
    check(!isCleared.value) { "Cannot access viewModelScope on a cleared ViewModel" }
    CoroutineScope(SupervisorJob() + viewModelScopeDispatcher())
  }
  public actual val viewModelScope: CoroutineScope by coroutineScopeLazy

  public actual constructor() : super() {
    closeables = linkedSetOf()
  }

  public actual constructor(vararg closeables: Closeable) : super() {
    this.closeables = LinkedHashSet(closeables.asList())
  }

  protected actual open fun onCleared(): Unit = Unit

  public actual fun addCloseable(closeable: Closeable) {
    // Although no logic should be done after user calls onCleared(), we will
    // ensure that if it has already been called, the closeable attempting to
    // be added will be closed immediately to ensure there will be no leaks.
    if (isCleared.value) {
      return closeable.closeWithRuntimeException()
    }
    synchronized(lockable) { closeables += closeable }
  }

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

      synchronized(lockable) {
        closeables.forEach { it.closeWithRuntimeException() }
        closeables.clear()

        onCleared()
      }
    }
  }

  /**
   * Returns `true` if this ViewModel has been cleared.
   */
  @InternalKmpViewModelApi
  public fun isCleared(): Boolean = isCleared.value
}

@Suppress("TooGenericExceptionCaught")
private fun Closeable.closeWithRuntimeException() {
  try {
    close()
  } catch (e: RuntimeException) {
    throw e
  } catch (e: Exception) {
    @Suppress("TooGenericExceptionThrown")
    throw RuntimeException(e)
  }
}
