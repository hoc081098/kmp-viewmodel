package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.Stable
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory

@Stable
public expect fun defaultCreationExtras(): CreationExtras

@Composable
public expect inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  extras: CreationExtras = defaultCreationExtras(),
  factory: ViewModelFactory<VM>,
): VM

@Composable
public inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  extras: CreationExtras = defaultCreationExtras(),
  crossinline factory: @DisallowComposableCalls CreationExtras.() -> VM,
): VM = kmpViewModel(
  key = key,
  extras = extras,
  factory = rememberViewModelFactory(factory),
)
