package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.ViewModel

@Composable
public fun <VM : ViewModel> rememberViewModelFactory(builder: () -> VM): ViewModelFactory<VM> =
  remember { DefaultViewModelFactory(builder) }

@Composable
public fun <VM : ViewModel> rememberViewModelFactory(key: Any?, builder: () -> VM): ViewModelFactory<VM> =
  remember(key) { DefaultViewModelFactory(builder) }
