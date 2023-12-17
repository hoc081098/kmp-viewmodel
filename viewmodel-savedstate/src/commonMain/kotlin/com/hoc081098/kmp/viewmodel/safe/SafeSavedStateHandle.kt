package com.hoc081098.kmp.viewmodel.safe

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import kotlin.jvm.JvmInline
import kotlinx.coroutines.flow.StateFlow

/**
 * A wrapper of [SavedStateHandle] that provides type-safe access with [NonNullSavedStateHandleKey].
 */
@Suppress("NOTHING_TO_INLINE")
@JvmInline
public value class SafeSavedStateHandle(public val savedStateHandle: SavedStateHandle) {
  /**
   * Get a value associated with the given [key].
   *
   * If no value is associated with the given [key], the [NonNullSavedStateHandleKey.defaultValue] will be returned.
   *
   * Otherwise, the value associated with the given [key] will be returned.
   * It maybe throw [kotlin.NullPointerException] if the value associated with the given [key] is null.
   *
   * @see [SavedStateHandle.get]
   * @see [SavedStateHandle.set]
   */
  public inline operator fun <T : Any> get(key: NonNullSavedStateHandleKey<T>): T =
    if (key.key in savedStateHandle) {
      savedStateHandle.get<T>(key.key)!!
    } else {
      key.defaultValue
    }

  /**
   * Get a value associated with the given [key].
   *
   * If no value is associated with the given [key], the [NullableSavedStateHandleKey.defaultValue] will be returned.
   *
   * Otherwise, the value associated with the given [key] will be returned (`null` is possible).
   *
   * @see [SavedStateHandle.get]
   * @see [SavedStateHandle.set]
   */
  public inline operator fun <T : Any> get(key: NullableSavedStateHandleKey<T>): T? =
    if (key.key in savedStateHandle) {
      @Suppress("RemoveExplicitTypeArguments")
      savedStateHandle.get<T?>(key.key)
    } else {
      key.defaultValue
    }

  /**
   * Associate the given value with the [key].
   *
   * @see [SavedStateHandle.set]
   */
  public inline operator fun <T : Any> set(key: NonNullSavedStateHandleKey<T>, value: T): Unit =
    savedStateHandle.set(key.key, value)

  /**
   * Associate the given value with the [key].
   *
   * @see [SavedStateHandle.set]
   */
  public inline operator fun <T : Any> set(key: NullableSavedStateHandleKey<T>, value: T?): Unit =
    savedStateHandle.set(key.key, value)

  /**
   * Returns a [StateFlow] that will emit the currently active value associated with the given [key].
   *
   * @see [SavedStateHandle.getStateFlow]
   */
  public inline fun <T : Any> getStateFlow(key: NonNullSavedStateHandleKey<T>): StateFlow<T> =
    savedStateHandle.getStateFlow(key.key, key.defaultValue)

  /**
   * Returns a [StateFlow] that will emit the currently active value associated with the given [key].
   *
   * @see [SavedStateHandle.getStateFlow]
   */
  public inline fun <T : Any> getStateFlow(key: NullableSavedStateHandleKey<T>): StateFlow<T?> =
    savedStateHandle.getStateFlow(key.key, key.defaultValue)

  /**
   * **Always throws [UnsupportedOperationException]**.
   */
  @Suppress("DeprecatedCallableAddReplaceWith")
  @Deprecated(
    message = "This method is not supported on non-null type key",
    level = DeprecationLevel.ERROR,
  )
  public inline fun <T : Any> remove(key: NonNullSavedStateHandleKey<T>): Nothing =
    throw UnsupportedOperationException("This method is unsupported on non-null type $key")

  /**
   * Removes a value associated with the given [key].
   *
   * After the removal, when calling [get] with the given [key],
   * the [NullableSavedStateHandleKey.defaultValue] will be returned.
   *
   * @see [SavedStateHandle.remove]
   */
  public inline fun <T : Any> remove(key: NullableSavedStateHandleKey<T>) {
    savedStateHandle.remove<Any?>(key.key)
  }
}

/**
 * Enables type-safe access to [SavedStateHandle].
 */
public inline fun <R> SavedStateHandle.safe(block: (SafeSavedStateHandle) -> R): R =
  block(SafeSavedStateHandle(this))
