package com.hoc081098.kmp.viewmodel.koin.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.InternalKmpViewModelApi
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.compose.LocalSavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.compose.defaultPlatformCreationExtras
import com.hoc081098.kmp.viewmodel.compose.defaultViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.compose.internal.kClassOf
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.koin.koinViewModelFactory
import kotlin.reflect.KClass
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

@Suppress("LongParameterList")
@OptIn(InternalKmpViewModelApi::class)
@MainThread
@Composable
public fun <VM : ViewModel> koinKmpViewModel(
  viewModelClass: KClass<VM>,
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  key: String? = null,
  extras: CreationExtras = defaultPlatformCreationExtras(),
  savedStateHandleFactory: SavedStateHandleFactory? = LocalSavedStateHandleFactory.current,
  qualifier: Qualifier? = null,
  scope: Scope = currentKoinScope(),
  parameters: ParametersDefinition? = null,
): VM {
  val factory = remember(viewModelClass, scope, qualifier, parameters) {
    koinViewModelFactory(
      viewModelClass = viewModelClass,
      scope = scope,
      qualifier = qualifier,
      parameters = parameters,
    )
  }

  val vmKey = remember(qualifier, scope, key) {
    getViewModelKey(qualifier, scope, key)
  }

  return kmpViewModel(
    factory = factory,
    viewModelStoreOwner = viewModelStoreOwner,
    key = vmKey,
    extras = extras,
    savedStateHandleFactory = savedStateHandleFactory,
  )
}

@Suppress("LongParameterList")
@OptIn(InternalKmpViewModelApi::class)
@MainThread
@Composable
@NonRestartableComposable
public inline fun <reified VM : ViewModel> koinKmpViewModel(
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  key: String? = null,
  extras: CreationExtras = defaultPlatformCreationExtras(),
  savedStateHandleFactory: SavedStateHandleFactory? = LocalSavedStateHandleFactory.current,
  qualifier: Qualifier? = null,
  scope: Scope = currentKoinScope(),
  noinline parameters: ParametersDefinition? = null,
): VM =
  koinKmpViewModel(
    viewModelClass = kClassOf<VM>(),
    viewModelStoreOwner = viewModelStoreOwner,
    key = key,
    extras = extras,
    savedStateHandleFactory = savedStateHandleFactory,
    qualifier = qualifier,
    scope = scope,
    parameters = parameters,
  )

// Copied from https://github.com/InsertKoinIO/koin/blob/e1f58a69d762d26c485b2ca7b7200e621c8ee6c0/android/koin-android/src/main/java/org/koin/androidx/viewmodel/GetViewModel.kt#L28
@InternalKmpViewModelApi
private fun getViewModelKey(qualifier: Qualifier?, scope: Scope, key: String?): String? {
  return if (qualifier == null && key == null && scope.isRoot) {
    null
  } else {
    val q = qualifier?.value ?: ""
    val k = key ?: ""
    val s = if (!scope.isRoot) scope.id else ""
    "$q$k$s"
  }
}
