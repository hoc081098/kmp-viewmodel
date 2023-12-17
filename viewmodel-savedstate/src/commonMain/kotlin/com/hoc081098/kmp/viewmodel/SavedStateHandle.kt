package com.hoc081098.kmp.viewmodel

import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.NullableSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.SafeSavedStateHandle
import kotlinx.coroutines.flow.StateFlow

/**
 * A multiplatform alias for `androidx.lifecycle.SavedStateHandle`
 * from the `androidx.lifecycle:lifecycle-viewmodel-savedstate` library.
 *
 * ### On Android
 *
 * This is a key-value map that will let you write and retrieve objects to and from the saved state.
 * These values will persist after the process is killed by the system and remain available via the same object.
 *
 * You can read a value from it via [get] or observe it via [StateFlow] returned by [getStateFlow].
 * You can write a value to it via [set].
 *
 * ### On other platforms
 *
 * This acts as a simple key-value map.
 *
 * ### Type-safe access
 *
 * You can use [SafeSavedStateHandle] with [NonNullSavedStateHandleKey] and [NullableSavedStateHandleKey]
 * to enable type-safe access to [SavedStateHandle]
 *
 * @see [SafeSavedStateHandle]
 * @see [NonNullSavedStateHandleKey]
 * @see [NullableSavedStateHandleKey]
 */
public expect class SavedStateHandle {
  /**
   * Creates a handle with the given initial arguments.
   *
   * @param initialState initial arguments for the [SavedStateHandle]
   */
  @Suppress("UnusedPrivateMember")
  public constructor(initialState: Map<String, Any?>)

  /**
   * Creates a handle with the empty state.
   */
  public constructor()

  /**
   * @param key The identifier for the value
   *
   * @return true if there is value associated with the given key.
   */
  @MainThread
  public operator fun contains(key: String): Boolean

  /**
   * Returns a value associated with the given key.
   *
   * @param key a key used to retrieve a value.
   */
  @MainThread
  public operator fun <T> get(key: String): T?

  /**
   * Returns a [StateFlow] that will emit the currently active value associated with the given
   * key.
   *
   * ```
   * val flow = savedStateHandle.getStateFlow(KEY, "defaultValue")
   * ```
   * Since this is a [StateFlow] there will always be a value available which, is why an initial
   * value must be provided. The value of this flow is changed by making a call to [set], passing
   * in the key that references this flow.
   *
   * If there is already a value associated with the given key, the initial value will be ignored.
   *
   * @param key The identifier for the flow
   * @param initialValue If no value exists with the given `key`, a new one is created
   * with the given `initialValue`.
   */
  @MainThread
  public fun <T> getStateFlow(key: String, initialValue: T): StateFlow<T>

  /**
   * Returns all keys contained in this [SavedStateHandle].
   */
  @MainThread
  public fun keys(): Set<String>

  /**
   * Removes a value associated with the given key. If there is a [StateFlow]
   * associated with the given key, they will be removed as well.
   *
   * All changes to [StateFlow]s previously returned by [SavedStateHandle.getStateFlow] won't be reflected in
   * the saved state. Also that `StateFlow` won't receive any updates about new
   * values associated by the given key.
   *
   * @param key a key
   * @return a value that was previously associated with the given key.
   */
  @MainThread
  public fun <T> remove(key: String): T?

  /**
   * Associate the given value with the key.
   * On Android, the value must have a type that could be stored in [android.os.Bundle].
   * On other platforms, the value can be of any type.
   *
   * This also sets values for any active [StateFlow]s.
   *
   * @param key a key used to associate with the given value.
   * @param value object of any type that can be accepted by Bundle.
   *
   * @throws IllegalArgumentException value cannot be saved in saved state
   */
  @MainThread
  public operator fun <T> set(key: String, value: T?): Unit
}
