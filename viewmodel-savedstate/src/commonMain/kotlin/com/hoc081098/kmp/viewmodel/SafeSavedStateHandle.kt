package com.hoc081098.kmp.viewmodel

import kotlin.jvm.JvmInline
import kotlinx.coroutines.flow.StateFlow

/**
 * A wrapper of [SavedStateHandle] that provides type-safe access with [SavedStateHandleKey].
 */
@Suppress("NOTHING_TO_INLINE")
@JvmInline
public value class SafeSavedStateHandle(public val savedStateHandle: SavedStateHandle) {
  public inline operator fun <T : Any> get(key: SavedStateHandleKey<T>): T =
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

  public inline operator fun <T : Any> set(key: SavedStateHandleKey<T>, value: T): Unit =
    savedStateHandle.set(key.key, value)

  public inline operator fun <T : Any> set(key: NullableSavedStateHandleKey<T>, value: T?): Unit =
    savedStateHandle.set(key.key, value)

  public inline fun <T : Any> getStateFlow(key: SavedStateHandleKey<T>): StateFlow<T> =
    savedStateHandle.getStateFlow(key.key, key.defaultValue)

  public inline fun <T : Any> getStateFlow(key: NullableSavedStateHandleKey<T>): StateFlow<T?> =
    savedStateHandle.getStateFlow(key.key, key.defaultValue)

  @Suppress("DeprecatedCallableAddReplaceWith")
  @Deprecated(
    message = "This method is not supported on non-null type key",
    level = DeprecationLevel.ERROR,
  )
  public inline fun <T : Any> remove(key: SavedStateHandleKey<T>): Nothing =
    throw UnsupportedOperationException("Not supported")

  public inline fun <T : Any> remove(key: NullableSavedStateHandleKey<T>) {
    savedStateHandle.remove<Any?>(key.key)
  }
}

public inline fun <R> SavedStateHandle.safe(block: (SafeSavedStateHandle) -> R): R =
  block(SafeSavedStateHandle(this))
