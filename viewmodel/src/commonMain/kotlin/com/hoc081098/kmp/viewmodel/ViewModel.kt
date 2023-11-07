package com.hoc081098.kmp.viewmodel

import kotlinx.coroutines.CoroutineScope

/**
 * The ViewModel class is a business logic or screen level state holder.
 * It exposes state to the UI and encapsulates related business logic.
 * Its principal advantage is that it caches state and persists it through configuration changes (on Android).
 */
public expect abstract class ViewModel {
  /**
   * Construct a new ViewModel instance.
   */
  public constructor()

  /**
   * Construct a new ViewModel instance.
   * Any [Closeable] objects provided here will be closed directly before [onCleared] is called.
   */
  @Suppress("UnusedPrivateMember")
  public constructor(vararg closeables: Closeable)

  /**
   * [CoroutineScope] tied to this [ViewModel].
   * This scope will be canceled when ViewModel will be cleared, i.e [ViewModel.onCleared] is called.
   *
   * ### On Android
   * - This scope is bound to
   * [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate].
   * - This scope is created lazily, and should be only accessed on the main thread.
   *
   *
   * ### Other platforms
   * - This scope is bound to the first available in the order:
   *   [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate],
   *   [Dispatchers.Main][kotlinx.coroutines.MainCoroutineDispatcher],
   *   [Dispatchers.Default][kotlinx.coroutines.Dispatchers.Default].
   *
   *   You should make sure that you add the corresponding dependency to your project,
   *   to ensure that the [kotlinx.coroutines.MainCoroutineDispatcher] is available
   *   (eg. `kotlinx-coroutines-swing`, `kotlinx-coroutines-javafx`, ...).
   * - This scope is created lazily, and can be accessed from any thread (it is thread-safe).
   */
  public val viewModelScope: CoroutineScope

  /**
   * This method will be called when this ViewModel is no longer used and will be destroyed.
   * It is useful when ViewModel observes some data and you need to clear this subscription to
   * prevent a leak of this ViewModel.
   *
   * - When overriding this method, make sure to call `super.onCleared()`.
   * - You should not call this method manually.
   */
  protected open fun onCleared()

  /**
   * Add a new [Closeable] object that will be closed directly before [onCleared] is called.
   *
   * If [onCleared] has already been called, the closeable will not be added,
   * and will instead be closed immediately.
   */
  public fun addCloseable(closeable: Closeable)
}
