package com.hoc081098.kmp.viewmodel

import kotlin.jvm.JvmSynthetic
import kotlinx.coroutines.flow.StateFlow

public class SavedStateHandleKey<T>(
  public val key: String,
  public val defaultValue: T,
) {

  public override fun toString(): String = "SavedStateHandleKey('$key', $defaultValue)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SavedStateHandleKey<*>) return false

    return key == other.key
  }

  override fun hashCode(): Int = key.hashCode()
}

@Suppress("NOTHING_TO_INLINE")
@JvmSynthetic
public inline operator fun <T> SavedStateHandle.get(key: SavedStateHandleKey<T>): T =
  if (key.key in this) {
    @Suppress("UNCHECKED_CAST", "RemoveExplicitTypeArguments")
    get<T>(key.key) as T
  } else {
    set(key, key.defaultValue)
    key.defaultValue
  }

@Suppress("NOTHING_TO_INLINE")
@JvmSynthetic
public inline operator fun <T> SavedStateHandle.set(key: SavedStateHandleKey<T>, value: T): Unit =
  set(key.key, value)

@Suppress("NOTHING_TO_INLINE")
@JvmSynthetic
public inline fun <T> SavedStateHandle.getStateFlow(key: SavedStateHandleKey<T>): StateFlow<T> =
  getStateFlow(key.key, key.defaultValue)
