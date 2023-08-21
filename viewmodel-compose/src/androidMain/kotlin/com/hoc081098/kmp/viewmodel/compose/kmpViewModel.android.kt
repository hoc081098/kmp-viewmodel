package com.hoc081098.kmp.viewmodel.compose

import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.viewmodel.CreationExtras.Empty
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.MutableCreationExtras
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.toAndroidX

@PublishedApi
@JvmField
internal val DefaultCreationExtrasForAndroid: CreationExtras = MutableCreationExtras()

@MainThread
@Composable
public actual inline fun <reified VM : ViewModel> kmpViewModel(
  key: String?,
  extras: CreationExtras,
  viewModelStoreOwner: ViewModelStoreOwner,
  factory: ViewModelFactory<VM>,
): VM = resolveViewModel(
  modelClass = VM::class.java,
  viewModelStoreOwner = viewModelStoreOwner,
  key = key,
  factory = factory,
  extras = extras,
)

@MainThread
@PublishedApi
@Composable
internal fun <VM : ViewModel> resolveViewModel(
  modelClass: Class<VM>,
  viewModelStoreOwner: ViewModelStoreOwner,
  key: String?,
  factory: ViewModelFactory<VM>,
  extras: CreationExtras,
): VM {
  val androidXOwner = remember(viewModelStoreOwner, viewModelStoreOwner::toAndroidX)

  return viewModel(
    modelClass = modelClass,
    viewModelStoreOwner = androidXOwner,
    key = key,
    factory = remember(factory, factory::toAndroidX),
    extras = if (extras === DefaultCreationExtrasForAndroid) {
      if (androidXOwner is HasDefaultViewModelProviderFactory) {
        androidXOwner.defaultViewModelCreationExtras
      } else {
        Empty
      }
    } else {
      extras
    },
  )
}

@Stable
public actual fun defaultCreationExtras(): CreationExtras = DefaultCreationExtrasForAndroid
