package com.hoc081098.kmp.viewmodel.compose

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hoc081098.kmp.viewmodel.ViewModel

@Composable
public actual inline fun <reified VM : ViewModel> kmpViewModel(
  key: String?,
  factory: ViewModelFactory<VM>,
): VM = viewModel(
  key = key,
  factory = factory.toAndroidXFactory(),
  viewModelStoreOwner = checkNotNull(
    LocalViewModelStoreOwner.current
      ?: findViewModelStoreOwner(LocalContext.current),
  ) {
    "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
  },
)

@PublishedApi
internal inline fun <reified VM : ViewModel> ViewModelFactory<VM>.toAndroidXFactory(): ViewModelProvider.Factory =
  object : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
      @Suppress("UNCHECKED_CAST")
      return this@toAndroidXFactory.create() as T
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
