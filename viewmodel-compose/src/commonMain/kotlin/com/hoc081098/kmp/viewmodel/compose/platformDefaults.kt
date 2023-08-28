package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner

@Stable
public expect fun defaultPlatformCreationExtras(): CreationExtras

@MainThread
@Composable
public expect fun defaultPlatformViewModelStoreOwner(): ViewModelStoreOwner
