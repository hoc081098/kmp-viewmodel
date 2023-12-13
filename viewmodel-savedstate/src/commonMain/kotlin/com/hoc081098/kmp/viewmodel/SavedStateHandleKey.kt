package com.hoc081098.kmp.viewmodel

import kotlin.jvm.JvmSynthetic
import kotlinx.coroutines.flow.StateFlow

/**
 * Check if [value] is valid to put into [SavedStateHandle].
 */
internal expect fun validateValue(value: Any?): Boolean

/**
 * Key for values stored in [SavedStateHandle].
 * Type [T] is the type of the value associated with the key.
 *
 * - On `Android` platform, the [T] must be one type that could be stored in [android.os.Bundle].
 *   If this requirement is not met, an [IllegalArgumentException] will be thrown at the construction time.
 *
 * - On `non-Android` platform, the [T] could be any type.
 *
 * @see SavedStateHandle.set
 */
public class SavedStateHandleKey<T>(
  public val key: String,
  public val defaultValue: T,
) {
  init {
    require(validateValue(defaultValue)) {
      "Can't put value with type ${defaultValue!!::class} into saved state"
    }
  }

  public override fun toString(): String = "SavedStateHandleKey(key='$key', defaultValue=$defaultValue)"

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
