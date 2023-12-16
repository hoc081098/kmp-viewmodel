package com.hoc081098.kmp.viewmodel.safe

import com.hoc081098.kmp.viewmodel.SavedStateHandle

/**
 * Key for values stored in [SavedStateHandle].
 * Type [T] is the type of the value associated with the key.
 * The value associated with the key may be null.
 *
 * @param key the key
 * @param defaultValue the default value is used if no value is associated with the key
 *
 * @see SafeSavedStateHandle
 */
public class NullableSavedStateHandleKey<T : Any> internal constructor(
  public val key: String,
  public val defaultValue: T?,
) {
  public override fun toString(): String = "NullableSavedStateHandleKey(key='$key', defaultValue=$defaultValue)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is NullableSavedStateHandleKey<*>) return false
    return key == other.key && defaultValue == other.defaultValue
  }

  override fun hashCode(): Int = 31 * key.hashCode() + defaultValue.hashCode()

  public companion object
}
