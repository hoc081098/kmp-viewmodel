package com.hoc081098.kmp.viewmodel.safe

import com.hoc081098.kmp.viewmodel.SavedStateHandle

/**
 * Key for values stored in [SavedStateHandle].
 * Type [T] is the type of the value associated with the key.
 * The value associated with the key must not be null.
 *
 * @see SafeSavedStateHandle
 */
public class NonNullSavedStateHandleKey<T : Any> internal constructor(
  public val key: String,
  public val defaultValue: T,
) {
  public override fun toString(): String = "NonNullSavedStateHandleKey(key='$key', defaultValue=$defaultValue)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is NonNullSavedStateHandleKey<*>) return false
    return key == other.key && defaultValue == other.defaultValue
  }

  override fun hashCode(): Int = 31 * key.hashCode() + defaultValue.hashCode()

  public companion object
}
