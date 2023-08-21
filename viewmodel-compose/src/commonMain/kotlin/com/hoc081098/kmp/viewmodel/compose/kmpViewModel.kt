package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.Stable
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner

@Stable
public expect fun defaultPlatformCreationExtras(): CreationExtras

@MainThread
@Composable
public expect fun defaultPlatformViewModelStoreOwner(): ViewModelStoreOwner

@MainThread
@Composable
public expect inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  extras: CreationExtras = defaultPlatformCreationExtras(),
  viewModelStoreOwner: ViewModelStoreOwner = defaultPlatformViewModelStoreOwner(),
  factory: ViewModelFactory<VM>,
): VM

@MainThread
@Composable
public inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  viewModelStoreOwner: ViewModelStoreOwner = defaultPlatformViewModelStoreOwner(),
  extras: CreationExtras = defaultPlatformCreationExtras(),
  crossinline factory: @DisallowComposableCalls CreationExtras.() -> VM,
): VM = kmpViewModel(
  key = key,
  extras = extras,
  viewModelStoreOwner = viewModelStoreOwner,
  factory = rememberViewModelFactory(factory),
)
