package com.hoc081098.kmp.viewmodel

import kotlin.jvm.JvmInline
import kotlinx.coroutines.flow.StateFlow

/**
 * Key for values stored in [SavedStateHandle].
 * Type [T] is the type of the value associated with the key.
 *
 * @see SafeSavedStateHandle
 */
public class SavedStateHandleKey<out T> internal constructor(
  public val key: String,
  public val defaultValue: T,
) {
  public override fun toString(): String = "SavedStateHandleKey(key='$key', defaultValue=$defaultValue)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SavedStateHandleKey<*>) return false
    return key == other.key
  }

  override fun hashCode(): Int = key.hashCode()
}

/**
 * A wrapper of [SavedStateHandle] that provides type-safe access with [SavedStateHandleKey].
 */
@Suppress("NOTHING_TO_INLINE")
@JvmInline
public value class SafeSavedStateHandle(public val savedStateHandle: SavedStateHandle) {
  public inline operator fun <T> get(key: SavedStateHandleKey<T>): T =
    if (key.key in savedStateHandle) {
      // DO NOT use "!!" here, because [T] may be nullable type.
      @Suppress("UNCHECKED_CAST", "RemoveExplicitTypeArguments")
      savedStateHandle.get<T>(key.key) as T
    } else {
      set(key, key.defaultValue)
      key.defaultValue
    }

  public inline operator fun <T> set(key: SavedStateHandleKey<T>, value: T): Unit =
    savedStateHandle.set(key.key, value)

  public inline fun <T> getStateFlow(key: SavedStateHandleKey<T>): StateFlow<T> =
    savedStateHandle.getStateFlow(key.key, key.defaultValue)
}

public inline fun <R> SavedStateHandle.safe(block: (SafeSavedStateHandle) -> R): R =
  block(SafeSavedStateHandle(this))
