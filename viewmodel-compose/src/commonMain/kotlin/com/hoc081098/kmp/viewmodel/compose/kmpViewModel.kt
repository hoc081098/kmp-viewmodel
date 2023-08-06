package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.ViewModel

@Composable
public expect inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  factory: ViewModelFactory<VM>,
): VM

@Composable
public inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  crossinline factory: @DisallowComposableCalls () -> VM,
): VM = kmpViewModel(
  key = key,
  factory = remember { viewModelFactory(factory) },
)
