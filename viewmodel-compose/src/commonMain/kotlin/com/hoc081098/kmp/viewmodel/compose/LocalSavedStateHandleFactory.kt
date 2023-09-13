package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory

/**
 * The CompositionLocal containing the current [SavedStateHandleFactory] or `null` if not provided.
 */
public val LocalSavedStateHandleFactory: ProvidableCompositionLocal<SavedStateHandleFactory?> =
  staticCompositionLocalOf { null }
