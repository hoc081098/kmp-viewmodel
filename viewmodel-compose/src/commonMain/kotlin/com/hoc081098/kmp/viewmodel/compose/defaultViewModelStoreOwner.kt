package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import kotlin.jvm.JvmSynthetic

/**
 * Returns current composition local value for the [ViewModelStoreOwner] provided by [LocalViewModelStoreOwner].
 * or [defaultPlatformViewModelStoreOwner] if one has not been provided.
 */
@Suppress("NOTHING_TO_INLINE")
@JvmSynthetic
@MainThread
@Composable
public inline fun defaultViewModelStoreOwner(): ViewModelStoreOwner =
  LocalViewModelStoreOwner.current
    ?: defaultPlatformViewModelStoreOwner()
