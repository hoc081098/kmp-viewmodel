package com.hoc081098.kmp.viewmodel.safe

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import dev.drewhamilton.poko.ArrayContentBased
import dev.drewhamilton.poko.ArrayContentSupport
import dev.drewhamilton.poko.Poko

/**
 * Key for values stored in [SavedStateHandle].
 * Type [T] is the type of the value associated with the key.
 * The value associated with the key must not be null.
 *
 * @param key the key
 * @param defaultValue the default value is used if no value is associated with the key
 *
 * @see SafeSavedStateHandle
 */
@OptIn(ArrayContentSupport::class)
@Poko
public class NonNullSavedStateHandleKey<T : Any> internal constructor(
  public val key: String,
  @ArrayContentBased public val defaultValue: T,
) {
  public companion object
}
