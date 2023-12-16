package com.hoc081098.kmp.viewmodel.safe

/**
 * Key for values stored in [SavedStateHandle].
 * Type [T] is the type of the value associated with the key.
 *
 * @see SafeSavedStateHandle
 */
public class SavedStateHandleKey<T : Any> internal constructor(
  public val key: String,
  public val defaultValue: T,
) {
  public override fun toString(): String = "SavedStateHandleKey(key='$key', defaultValue=$defaultValue)"

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SavedStateHandleKey<*>) return false
    return key == other.key && defaultValue == other.defaultValue
  }

  override fun hashCode(): Int = 31 * key.hashCode() + defaultValue.hashCode()

  public companion object
}
