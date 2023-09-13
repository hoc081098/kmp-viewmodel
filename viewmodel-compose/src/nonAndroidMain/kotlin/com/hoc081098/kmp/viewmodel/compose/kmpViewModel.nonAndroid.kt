package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.EmptyCreationExtras
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import kotlin.reflect.KClass

@MainThread
@Composable
public actual inline fun <reified VM : ViewModel> kmpViewModel(
  factory: ViewModelFactory<VM>,
  viewModelStoreOwner: ViewModelStoreOwner,
  key: String?,
  extras: CreationExtras,
  savedStateHandleFactory: SavedStateHandleFactory?,
): VM {
  val kClass = remember { VM::class }

  return resolveViewModel(
    key = key ?: rememberDefaultViewModelKey(kClass),
    kClass = kClass,
    factory = factory,
    extras = extras,
    viewModelStoreOwner = viewModelStoreOwner,
    savedStateHandleFactory = savedStateHandleFactory,
  )
}

@MainThread
@PublishedApi
internal fun <VM : ViewModel> resolveViewModel(
  key: String,
  kClass: KClass<VM>,
  extras: CreationExtras,
  factory: ViewModelFactory<VM>,
  viewModelStoreOwner: ViewModelStoreOwner,
  savedStateHandleFactory: SavedStateHandleFactory?,
): VM = viewModelStoreOwner.getOrCreateViewModel(
  key = key,
  kClass = kClass,
  factory = factory,
  extras = extras,
  savedStateHandleFactory = savedStateHandleFactory,
)

@Stable
public actual fun defaultPlatformCreationExtras(): CreationExtras = EmptyCreationExtras

@MainThread
@Composable
public actual fun defaultPlatformViewModelStoreOwner(): ViewModelStoreOwner = rememberDefaultViewModelStoreOwner()
