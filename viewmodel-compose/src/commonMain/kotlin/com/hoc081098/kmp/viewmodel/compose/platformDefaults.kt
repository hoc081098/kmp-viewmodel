package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner

/**
 * Returns the default [CreationExtras] for the current platform.
 */
@Stable
public expect fun defaultPlatformCreationExtras(): CreationExtras

/**
 * Returns the default [ViewModelStoreOwner] for the current platform.
 */
@MainThread
@Composable
public expect fun defaultPlatformViewModelStoreOwner(): ViewModelStoreOwner
