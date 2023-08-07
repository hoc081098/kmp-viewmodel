package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.viewModelFactory

@Composable
public inline fun <VM : ViewModel> rememberViewModelFactory(
  crossinline builder: @DisallowComposableCalls CreationExtras.() -> VM,
): ViewModelFactory<VM> =
  remember { viewModelFactory(builder) }

@Composable
public inline fun <VM : ViewModel> rememberViewModelFactory(
  key: Any?,
  crossinline builder: @DisallowComposableCalls CreationExtras.() -> VM,
): ViewModelFactory<VM> =
  remember(key) { viewModelFactory(builder) }
