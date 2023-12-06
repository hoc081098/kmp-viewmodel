package com.hoc081098.kmp.viewmodel.compose

import androidx.lifecycle.ViewModelStoreOwner as AndroidXViewModelStoreOwner
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.toKmp

/**
 * Returns the nearest [androidx.lifecycle.ViewModelStoreOwner] that is provided via [LocalViewModelStoreOwner].
 * If it is not provided, then the [androidx.lifecycle.ViewModelStoreOwner] is searched in the hierarchy of [Context]s.
 * If no [androidx.lifecycle.ViewModelStoreOwner] is found, an [IllegalStateException] will be thrown.
 */
@Composable
@MainThread
public actual fun defaultPlatformViewModelStoreOwner(): ViewModelStoreOwner =
  currentAndroidXViewModelStoreOwner().toKmp()

@Suppress("NOTHING_TO_INLINE")
@Composable
private inline fun currentAndroidXViewModelStoreOwner(): AndroidXViewModelStoreOwner = checkNotNull(
  LocalViewModelStoreOwner.current
    ?: findViewModelStoreOwner(LocalContext.current),
) {
  "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
}

/**
 * Find the nearest [androidx.lifecycle.ViewModelStoreOwner] in the hierarchy of [Context]s.
 */
private fun findViewModelStoreOwner(context: Context): AndroidXViewModelStoreOwner? {
  var innerContext = context
  while (innerContext is ContextWrapper) {
    if (innerContext is AndroidXViewModelStoreOwner) {
      return innerContext
    }
    innerContext = innerContext.baseContext
  }
  return null
}
