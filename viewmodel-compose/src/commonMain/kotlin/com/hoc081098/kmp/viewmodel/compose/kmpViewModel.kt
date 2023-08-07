package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.EmptyCreationExtras
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory

@Composable
public expect inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  extras: CreationExtras = EmptyCreationExtras,
  factory: ViewModelFactory<VM>,
): VM

@Composable
public inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  extras: CreationExtras = EmptyCreationExtras,
  crossinline factory: @DisallowComposableCalls CreationExtras.() -> VM,
): VM = kmpViewModel(
  key = key,
  extras = extras,
  factory = rememberViewModelFactory(factory),
)
