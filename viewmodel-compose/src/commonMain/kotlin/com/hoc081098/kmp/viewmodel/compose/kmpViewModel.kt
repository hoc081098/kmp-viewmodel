package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import com.hoc081098.kmp.viewmodel.ViewModel

@Composable
public expect inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  factory: ViewModelFactory<VM>,
): VM

@Composable
public inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  noinline factory: () -> VM,
): VM = kmpViewModel(
  key = key,
  factory = rememberViewModelFactory(factory),
)
