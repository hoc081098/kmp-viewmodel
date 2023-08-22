package com.hoc081098.kmp.viewmodel.compose

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner as AndroidXViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.toKmp

@Composable
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
