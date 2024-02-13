@file:Suppress("standard:no-empty-file")
// TODO: Use reified type parameter when KT-57727 is fixed
// TODO: Issue https://github.com/JetBrains/compose-multiplatform/issues/3147
// TODO: Issue https://youtrack.jetbrains.com/issue/KT-57727

package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.compose.internal.kClassOf
import com.hoc081098.kmp.viewmodel.viewModelFactory

/**
 * Remember a [ViewModelFactory] that will be used to create a [ViewModel].
 *
 * @see remember
 * @see viewModelFactory
 */
@OptIn(InternalKmpViewModelApi::class)
@Composable
public inline fun <reified VM : ViewModel> rememberViewModelFactory(
  crossinline builder: @DisallowComposableCalls CreationExtras.() -> VM,
): ViewModelFactory<VM> =
  remember {
    viewModelFactory(
      viewModelClass = kClassOf<VM>(),
      builder = builder,
    )
  }

/**
 * Remember a [ViewModelFactory] that will be used to create a [ViewModel] with the given [key].
 *
 * @see remember
 * @see viewModelFactory
 */
@OptIn(InternalKmpViewModelApi::class)
@Composable
public inline fun <reified VM : ViewModel> rememberViewModelFactory(
  key: Any?,
  crossinline builder: @DisallowComposableCalls CreationExtras.() -> VM,
): ViewModelFactory<VM> =
  remember(key) {
    viewModelFactory(
      viewModelClass = kClassOf<VM>(),
      builder = builder,
    )
  }
