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
public expect fun defaultCreationExtras(): CreationExtras

@MainThread
@Composable
public expect fun defaultViewModelStoreOwner(): ViewModelStoreOwner

@MainThread
@Composable
public expect inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  extras: CreationExtras = defaultCreationExtras(),
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  factory: ViewModelFactory<VM>,
): VM

@MainThread
@Composable
public inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  extras: CreationExtras = defaultCreationExtras(),
  crossinline factory: @DisallowComposableCalls CreationExtras.() -> VM,
): VM = kmpViewModel(
  key = key,
  extras = extras,
  viewModelStoreOwner = viewModelStoreOwner,
  factory = rememberViewModelFactory(factory),
)
