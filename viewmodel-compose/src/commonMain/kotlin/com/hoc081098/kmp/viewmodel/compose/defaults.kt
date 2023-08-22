package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner

@MainThread
@Composable
public fun defaultViewModelStoreOwner(): ViewModelStoreOwner =
  LocalViewModelStoreOwner.current
    ?: defaultPlatformViewModelStoreOwner()
