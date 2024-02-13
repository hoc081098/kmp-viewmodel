package com.hoc081098.kmp.viewmodel.safe

import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import dev.drewhamilton.poko.ArrayContentBased
import dev.drewhamilton.poko.ArrayContentSupport
import dev.drewhamilton.poko.Poko

/**
 * Key for values stored in [SavedStateHandle].
 * The type of the value associated with this key is [T] (non-null)
 *
 * **NOTE**: When using [key] directly, you must ensure that the value associated with the key has **type `T`**.
 * Otherwise, it may throw [kotlin.ClassCastException] or [kotlin.NullPointerException] when using this key
 * with [SafeSavedStateHandle].
 *
 * @param key the key
 * @param defaultValue the default value is used if no value is associated with the key
 *
 * @see SafeSavedStateHandle
 */
@OptIn(ArrayContentSupport::class)
@Poko
public class NonNullSavedStateHandleKey<T : Any> @InternalKmpViewModelApi
@PublishedApi
internal constructor(
  /**
   * **NOTE**: When using [key] directly, you must ensure that the value associated with the key has **type `T`**.
   * Otherwise, it may throw [kotlin.ClassCastException] or [kotlin.NullPointerException] when using this key
   * with [SafeSavedStateHandle].
   */
  @DelicateSafeSavedStateHandleApi
  public val key: String,
  @ArrayContentBased public val defaultValue: T,
  internal val transform: ((Any?) -> T)? = null,
) {
  public companion object
}
