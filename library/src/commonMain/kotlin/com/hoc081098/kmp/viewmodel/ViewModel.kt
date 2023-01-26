package com.hoc081098.kmp.viewmodel

import kotlinx.coroutines.CoroutineScope

/**
 * The ViewModel class is a business logic or screen level state holder.
 * It exposes state to the UI and encapsulates related business logic.
 * Its principal advantage is that it caches state and persists it through configuration changes (on Android).
 */
public expect abstract class ViewModel() {
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
   * - This scope is created lazily, and can be accessed from any thread (it is thread-safe).
   */
  protected val viewModelScope: CoroutineScope

  /**
   * This method will be called when this ViewModel is no longer used and will be destroyed.
   * It is useful when ViewModel observes some data and you need to clear this subscription to
   * prevent a leak of this ViewModel.
   *
   * - When overriding this method, make sure to call `super.onCleared()`.
   * - You should not call this method manually.
   */
  protected open fun onCleared()
}