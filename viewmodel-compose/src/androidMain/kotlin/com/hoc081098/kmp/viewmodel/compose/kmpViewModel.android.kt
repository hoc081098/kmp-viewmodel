package com.hoc081098.kmp.viewmodel.compose

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras.Empty
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.MutableCreationExtras
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory

@PublishedApi
@JvmField
internal val DefaultCreationExtrasForAndroid: CreationExtras = MutableCreationExtras()

@Composable
public actual inline fun <reified VM : ViewModel> kmpViewModel(
  key: String?,
  extras: CreationExtras,
  factory: ViewModelFactory<VM>,
): VM {
  val viewModelStoreOwner = getViewModelStoreOwner()

  val defaultExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
    viewModelStoreOwner.defaultViewModelCreationExtras
  } else {
    Empty
  }

  return viewModel(
    key = key,
    factory = remember(factory, factory::toAndroidXFactory),
    extras = if (extras === DefaultCreationExtrasForAndroid) {
      defaultExtras
    } else {
      extras
    },
    viewModelStoreOwner = viewModelStoreOwner,
  )
}

@Suppress("NOTHING_TO_INLINE")
@PublishedApi
@Composable
internal inline fun getViewModelStoreOwner(): ViewModelStoreOwner = checkNotNull(
  LocalViewModelStoreOwner.current
    ?: findViewModelStoreOwner(LocalContext.current),
) {
  "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
}

@Stable
public actual fun defaultCreationExtras(): CreationExtras = DefaultCreationExtrasForAndroid

@PublishedApi
internal inline fun <reified VM : ViewModel> ViewModelFactory<VM>.toAndroidXFactory(): ViewModelProvider.Factory =
  object : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
      @Suppress("UNCHECKED_CAST")
      return this@toAndroidXFactory.create(extras) as T
    }
  }

@PublishedApi
internal fun findViewModelStoreOwner(context: Context): ViewModelStoreOwner? {
  var innerContext = context
  while (innerContext is ContextWrapper) {
    if (innerContext is ViewModelStoreOwner) {
      return innerContext
    }
    innerContext = innerContext.baseContext
  }
  return null
}
