package com.hoc081098.kmp.viewmodel.safe

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.safe.internal.mapState
import kotlin.jvm.JvmInline
import kotlinx.coroutines.flow.StateFlow

/**
 * A wrapper of [SavedStateHandle] that provides type-safe access
 * with [NonNullSavedStateHandleKey]s and [NullableSavedStateHandleKey]s.
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(DelicateSafeSavedStateHandleApi::class)
@JvmInline
public value class SafeSavedStateHandle(public val savedStateHandle: SavedStateHandle) {
  /**
   * Get a value associated with the given [key].
   *
   * If no value is associated with the given [key], the [NonNullSavedStateHandleKey.defaultValue] will be returned.
   *
   * Otherwise, the value associated with the given [key] will be returned.
   * It may throw [kotlin.NullPointerException] if the value associated with the given [key] is null.
   *
   * @throws [kotlin.NullPointerException] if the value associated with the given [key] is null.
   * @see [SavedStateHandle.get]
   * @see [SavedStateHandle.set]
   */
  public operator fun <T : Any> get(key: NonNullSavedStateHandleKey<T>): T =
    if (key.key in savedStateHandle) {
      val transform = key.transform

      if (transform == null) {
        savedStateHandle.get<T>(key.key)!!
      } else {
        @Suppress("RemoveExplicitTypeArguments") // Readability
        transform(savedStateHandle.get<Any?>(key.key))
      }
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
  public operator fun <T : Any> get(key: NullableSavedStateHandleKey<T>): T? =
    if (key.key in savedStateHandle) {
      val transform = key.transform

      @Suppress("RemoveExplicitTypeArguments") // Readability
      if (transform == null) {
        savedStateHandle.get<T?>(key.key)
      } else {
        transform(savedStateHandle.get<Any?>(key.key))
      }
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
  public fun <T : Any> getStateFlow(key: NonNullSavedStateHandleKey<T>): StateFlow<T> {
    val transform = key.transform

    return if (transform == null) {
      @Suppress("RemoveExplicitTypeArguments") // Readability
      savedStateHandle.getStateFlow<T>(key.key, key.defaultValue)
    } else {
      savedStateHandle
        .getStateFlow<Any>(key.key, key.defaultValue)
        .mapState(transform)
    }
  }

  /**
   * Returns a [StateFlow] that will emit the currently active value associated with the given [key].
   *
   * @see [SavedStateHandle.getStateFlow]
   */
  public fun <T : Any> getStateFlow(key: NullableSavedStateHandleKey<T>): StateFlow<T?> {
    val transform = key.transform

    return if (transform == null) {
      @Suppress("RemoveExplicitTypeArguments") // Readability
      savedStateHandle.getStateFlow<T?>(key.key, key.defaultValue)
    } else {
      savedStateHandle
        .getStateFlow<Any?>(key.key, key.defaultValue)
        .mapState(transform)
    }
  }

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
 * You can use this with [NonNullSavedStateHandleKey]s and [NullableSavedStateHandleKey]s.
 *
 * Example of usage:
 *
 * ```kotlin
 * val idKey = NullableSavedStateHandleKey.int("id")
 *
 * // Get a value associated with the given key.
 * val id: Int? = savedStateHandle.safe { it[idKey] }
 *
 * // Associate the given value with the key.
 * savedStateHandle.safe { it[idKey] = 42 }
 *
 * // Remove a value associated with the given key.
 * savedStateHandle.safe { it.remove(idKey) }
 *
 * // Get a StateFlow
 * val idStateFlow: StateFlow<Int?> = savedStateHandle.safe { it.getStateFlow(idKey) }
 * ```
 *
 * @receiver [SavedStateHandle] to be wrapped and accessed safely.
 * @param block a block of code to be executed with [SafeSavedStateHandle].
 * Inside the block, you can use methods of [SafeSavedStateHandle] to access [SavedStateHandle].
 */
public inline fun <R> SavedStateHandle.safe(block: (SafeSavedStateHandle) -> R): R =
  block(SafeSavedStateHandle(this))
