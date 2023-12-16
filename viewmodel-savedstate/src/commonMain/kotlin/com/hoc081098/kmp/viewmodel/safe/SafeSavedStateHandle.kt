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
  public inline operator fun <T : Any> get(key: NonNullSavedStateHandleKey<T>): T =
    if (key.key in savedStateHandle) {
      savedStateHandle.get<T>(key.key)!!
    } else {
      key.defaultValue.also { this[key] = it }
    }

  public inline operator fun <T : Any> get(key: NullableSavedStateHandleKey<T>): T? =
    if (key.key in savedStateHandle) {
      savedStateHandle.get<T>(key.key)
    } else {
      key.defaultValue.also { this[key] = it }
    }

  public inline operator fun <T : Any> set(key: NonNullSavedStateHandleKey<T>, value: T): Unit =
    savedStateHandle.set(key.key, value)

  public inline operator fun <T : Any> set(key: NullableSavedStateHandleKey<T>, value: T?): Unit =
    savedStateHandle.set(key.key, value)

  public inline fun <T : Any> getStateFlow(key: NonNullSavedStateHandleKey<T>): StateFlow<T> =
    savedStateHandle.getStateFlow(key.key, key.defaultValue)

  public inline fun <T : Any> getStateFlow(key: NullableSavedStateHandleKey<T>): StateFlow<T?> =
    savedStateHandle.getStateFlow(key.key, key.defaultValue)

  @Suppress("DeprecatedCallableAddReplaceWith")
  @Deprecated(
    message = "This method is not supported on non-null type key",
    level = DeprecationLevel.ERROR,
  )
  public inline fun <T : Any> remove(key: NonNullSavedStateHandleKey<T>): Nothing =
    throw UnsupportedOperationException("Not supported")

  public inline fun <T : Any> remove(key: NullableSavedStateHandleKey<T>) {
    savedStateHandle.remove<Any?>(key.key)
  }
}

/**
 * Enables type-safe access to [SavedStateHandle].
 */
public inline fun <R> SavedStateHandle.safe(block: (SafeSavedStateHandle) -> R): R =
  block(SafeSavedStateHandle(this))
